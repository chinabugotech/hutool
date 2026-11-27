# HKO calendar reference fixtures

Source: Hong Kong Observatory (香港天文台), Government of the Hong Kong Special Administrative Region, Gregorian-Lunar Calendar Conversion Table. The source data remain subject to the source owner’s terms, not the code’s Apache License: https://www.hko.gov.hk/en/readme/readme.htm .

- Overview: https://www.hko.gov.hk/tc/gts/time/conversion.htm
- Original annual text: `https://www.hko.gov.hk/tc/gts/time/calendar/text/files/T{YEAR}c.txt`
- Retrieved: 2026-11-26; years 1901–2100, inclusive.
- `lunar.csv.gz`: 73,049 consecutive civil dates with numeric lunar year, month, day and leap-month flag.
- `solar-terms.csv.gz`: 4,800 solar-term dates; index 0 is Minor Cold (小寒). Names retain HKO's traditional Chinese spelling.
- Both files are UTF-8 CSV compressed with gzip. Generate them locally under `hutool-core/target/hko-fixtures`; the complete HKO tables are not redistributed in this repository or in production JARs. Tests run offline after generation.

SHA-256 of the **uncompressed CSV**:

```text
970e0b804f5924aded7ccad8ad6c8da2abb5780ed1cdac7f8957dc9b46cf95d9  lunar.csv
4f12c658eed72fa07d273b2a1b30d81beac239b825d1d4de7d2267266f6cf138  solar-terms.csv
```

## Coverage and known differences

Hutool currently stores lunar years 1900–2099. The test compares both conversion directions for 72,723 dates, through 2100-02-08, and explicitly counts the remaining 326 reference dates as outside the supported lunar-year range. This fixture does not establish HKO coverage before 1901 or after 2100.

`known-differences.csv` records the exact 30 forward and 30 reverse differences from 2057-09-28 through 2057-10-27. The test compares the entire actual difference list with this file: additional, removed or changed differences fail the test. No whole year is ignored. The columns are direction, reference solar date, HKO lunar tuple, and actual result. Forward results are lunar tuples; reverse results are solar year/month/day (`-1/-1/-1` is the existing invalid-date sentinel).

The annual descriptors differ for 2057: Hutool currently uses `0x06b20`, whereas this HKO snapshot corresponds to `0x06aa0`. HKO explicitly notes uncertainty when a future new moon is close to midnight, including 2057-09-28. The existing discussion is https://github.com/jjonline/calendar.js/issues/11#issuecomment-607126524 . This baseline PR records that distinction without changing the selected annual descriptor. A subsequent data-alignment change should update the known differences in the same PR.

Lunar reverse conversion uses a fixed UTC+8 default time zone in this reference suite, restored afterwards. Time-zone independence needs separate regression tests. Solar-term fixtures describe civil dates, not exact astronomical instants or BaZi pillars.

## Refreshing

From the repository root, using Python 3.9 or newer:

```sh
python3 hutool-core/src/test/scripts/generate-hko-fixtures.py
mvn -pl hutool-core -am -Dhutool.hko.enabled=true -Dtest=ChineseCalendarHkoTest -Dsurefire.failIfNoSpecifiedTests=false test
```

Original annual responses are cached under `hutool-core/target/hko-source`. Pass `--refresh` to redownload them, or `--cache-dir PATH` to use a retained snapshot. The script validates daily continuity, lunar month lengths and the 24 ordered solar terms per year before writing either fixture, then prints their uncompressed hashes. Review changes against the raw HKO files and update the retrieval date and hashes above. Do not regenerate `known-differences.csv` automatically from the implementation under test.

The HKO suite is opt-in through `-Dhutool.hko.enabled=true`; ordinary unit tests do not download data. Once enabled, missing fixtures, incorrect hashes, missing dates and unexpected mismatches fail the test. `-Dhutool.hko.dir=/absolute/path` selects another local fixture directory. To review a refreshed snapshot, update both the expected hashes in the test and this README after inspecting the source differences.
