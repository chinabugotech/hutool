#!/usr/bin/env python3
"""Refresh offline calendar fixtures from HKO's 1901-2100 text calendars.

Run manually; tests never access the network. Python 3.9+ standard library only.
The cache stores original responses. Use --refresh to fetch a new snapshot.
"""
import argparse
import csv
import datetime
import gzip
import hashlib
import io
import re
import time
import urllib.request
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

URL = "https://www.hko.gov.hk/tc/gts/time/calendar/text/files/T{}c.txt"
MONTHS = dict(zip("正 二 三 四 五 六 七 八 九 十 十一 十二".split(), range(1, 13)))
DAYS = "初一 初二 初三 初四 初五 初六 初七 初八 初九 初十 十一 十二 十三 十四 十五 十六 十七 十八 十九 二十 廿一 廿二 廿三 廿四 廿五 廿六 廿七 廿八 廿九 三十".split()
TERMS = "小寒 大寒 立春 雨水 驚蟄 春分 清明 穀雨 立夏 小滿 芒種 夏至 小暑 大暑 立秋 處暑 白露 秋分 寒露 霜降 立冬 小雪 大雪 冬至".split()
ROW = re.compile(r"^(\d{4})年(\d{1,2})月(\d{1,2})日\s+(\S+)\s+星期[一二三四五六日天]\s*(\S+)?\s*$")


def download(year, cache, refresh):
    path = cache / ("T%dc.txt" % year)
    if refresh or not path.exists():
        for attempt in range(3):
            try:
                with urllib.request.urlopen(URL.format(year), timeout=30) as response:
                    data = response.read()
                path.write_bytes(data)
                break
            except (OSError, TimeoutError):
                if attempt == 2:
                    raise
                time.sleep(attempt + 1)
    data = path.read_bytes()
    try:
        return data.decode("utf-8-sig")
    except UnicodeDecodeError:
        return data.decode("big5")


def csv_bytes(header, rows):
    text = io.StringIO(newline="")
    writer = csv.writer(text, lineterminator="\n")
    writer.writerow(header)
    writer.writerows(rows)
    return text.getvalue().encode("utf-8")


def main():
    module = Path(__file__).resolve().parents[3]
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--cache-dir", type=Path, default=module / "target/hko-source")
    parser.add_argument("--output-dir", type=Path, default=module / "target/hko-fixtures")
    parser.add_argument("--refresh", action="store_true")
    args = parser.parse_args()
    args.cache_dir.mkdir(parents=True, exist_ok=True)
    years = list(range(1901, 2101))
    with ThreadPoolExecutor(max_workers=4) as pool:
        texts = list(pool.map(lambda y: download(y, args.cache_dir, args.refresh), years))
    lunar, solar = [], []
    year, month, leap = 1900, None, 0
    expected_date = datetime.date(1901, 1, 1)
    month_lengths = []
    previous_day = None
    for solar_year, text in zip(years, texts):
        rows = [match.groups() for line in text.splitlines() if (match := ROW.match(line))]
        if month is None:
            first_month = next(row[3] for row in rows if row[3].endswith("月"))
            month = MONTHS[first_month.removeprefix("閏")[:-1]]
            month = month if first_month.startswith("閏") else (month - 2) % 12 + 1
        terms = []
        for y, m, d, raw, term in rows:
            date = datetime.date(int(y), int(m), int(d))
            if date != expected_date or date.year != solar_year:
                raise ValueError("Missing, duplicate or out-of-order date: %s" % date)
            expected_date += datetime.timedelta(days=1)
            if raw.endswith("月"):
                if previous_day is not None:
                    month_lengths.append(previous_day)
                leap = int(raw.startswith("閏"))
                month = MONTHS[raw.removeprefix("閏")[:-1]]
                day = 1
                if month == 1 and not leap:
                    year = solar_year
            else:
                day = DAYS.index(raw) + 1
                if previous_day is not None and day != previous_day + 1:
                    raise ValueError("Non-sequential lunar day: %s" % date)
            previous_day = day
            lunar.append((date.isoformat(), year, month, day, leap))
            if term:
                terms.append(term)
                solar.append((date.isoformat(), TERMS.index(term), term))
        if terms != TERMS:
            raise ValueError("Expected 24 ordered solar terms in %s" % solar_year)
    if expected_date != datetime.date(2101, 1, 1) or len(lunar) != 73049:
        raise ValueError("Incomplete calendar range")
    if any(days not in (29, 30) for days in month_lengths):
        raise ValueError("Lunar month must have 29 or 30 days")
    outputs = {
        "lunar.csv.gz": csv_bytes(["solarDate", "lunarYear", "lunarMonth", "lunarDay", "isLeapMonth"], lunar),
        "solar-terms.csv.gz": csv_bytes(["solarDate", "solarTermIndex", "solarTermName"], solar),
    }
    args.output_dir.mkdir(parents=True, exist_ok=True)
    for name, data in outputs.items():
        # No timestamp or source filename in the gzip header.
        with (args.output_dir / name).open("wb") as stream:
            with gzip.GzipFile(fileobj=stream, mode="wb", filename="", mtime=0) as compressed:
                compressed.write(data)
        print("%s  %s (uncompressed SHA-256)" % (hashlib.sha256(data).hexdigest(), name), flush=True)


if __name__ == "__main__":
    main()
