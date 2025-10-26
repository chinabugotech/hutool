/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hutool.v7.core.math;

import java.io.Serial;
import java.math.BigInteger;
import java.util.Objects;

/**
 * {@code Fraction} 是一个 {@link Number} 实现，
 * 可以准确地存储分数。
 *
 * <p>此类是不可变的，并且与大多数接受
 * {@link Number} 的方法兼容。</p>
 *
 * <p>请注意，此类适用于常见用例，它是基于 <em>int</em> 的，
 * 因此容易受到各种溢出问题的影响。对于基于 BigInteger 的等效类，
 * 请参见 Commons Math 库中的 BigFraction 类。</p>
 * <p>
 * 此类来自：Apache Commons Lang3
 *
 * @since 6.0.0
 */
public final class Fraction extends Number implements Comparable<Fraction> {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * {@link Fraction} 表示 0.
	 */
	public static final Fraction ZERO = new Fraction(0, 1);
	/**
	 * {@link Fraction} 表示 1.
	 */
	public static final Fraction ONE = new Fraction(1, 1);

	/**
	 * 从 {@code double} 值创建一个 {@code Fraction} 实例。
	 *
	 * <p>此方法使用 <a href="https://web.archive.org/web/20210516065058/http%3A//archives.math.utk.edu/articles/atuyl/confrac/">
	 * 连分数算法</a>，最多计算 25 个收敛项，并将分母限制在 10,000 以内。</p>
	 *
	 * @param value 要转换的 double 值
	 * @return 一个新的分数实例，接近该值
	 * @throws ArithmeticException 如果 {@code |value| > Integer.MAX_VALUE} 或 {@code value = NaN}
	 * @throws ArithmeticException 如果计算出的分母为 {@code zero}
	 * @throws ArithmeticException 如果算法不收敛
	 */
	@SuppressWarnings("ReassignedVariable")
	public static Fraction of(double value) {
		final int sign = value < 0 ? -1 : 1;
		value = Math.abs(value);
		if (value > Integer.MAX_VALUE || Double.isNaN(value)) {
			throw new ArithmeticException("The value must not be greater than Integer.MAX_VALUE or NaN");
		}
		final int wholeNumber = (int) value;
		value -= wholeNumber;

		int numer0 = 0; // the pre-previous
		int denom0 = 1; // the pre-previous
		int numer1 = 1; // the previous
		int denom1 = 0; // the previous
		int numer2; // the current, setup in calculation
		int denom2; // the current, setup in calculation
		int a1 = (int) value;
		int a2;
		double x1 = 1;
		double x2;
		double y1 = value - a1;
		double y2;
		double delta1, delta2 = Double.MAX_VALUE;
		double fraction;
		int i = 1;
		do {
			delta1 = delta2;
			a2 = (int) (x1 / y1);
			x2 = y1;
			y2 = x1 - a2 * y1;
			numer2 = a1 * numer1 + numer0;
			denom2 = a1 * denom1 + denom0;
			fraction = (double) numer2 / (double) denom2;
			delta2 = Math.abs(value - fraction);
			a1 = a2;
			x1 = x2;
			y1 = y2;
			numer0 = numer1;
			denom0 = denom1;
			numer1 = numer2;
			denom1 = denom2;
			i++;
		} while (delta1 > delta2 && denom2 <= 10000 && denom2 > 0 && i < 25);
		if (i == 25) {
			throw new ArithmeticException("Unable to convert double to fraction");
		}
		return ofReduced((numer0 + wholeNumber * denom0) * sign, denom0);
	}

	/**
	 * 使用分数的两个部分 Y/Z 创建一个 {@code Fraction} 实例。
	 *
	 * <p>任何负号都会被解析到分子上。</p>
	 *
	 * @param numerator   分子，例如 '3/7' 中的 3
	 * @param denominator 分母，例如 '3/7' 中的 7
	 * @return 一个新的分数实例
	 * @throws ArithmeticException 如果分母为 {@code zero} 或分母为 {@code negative} 且分子为 {@code Integer#MIN_VALUE}
	 */
	public static Fraction of(int numerator, int denominator) {
		if (denominator == 0) {
			throw new ArithmeticException("The denominator must not be zero");
		}
		if (denominator < 0) {
			if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
				throw new ArithmeticException("overflow: can't negate");
			}
			numerator = -numerator;
			denominator = -denominator;
		}
		return new Fraction(numerator, denominator);
	}

	/**
	 * 使用分数的三个部分 X Y/Z 创建一个 {@code Fraction} 实例。
	 *
	 * <p>负号必须传递到整数部分。</p>
	 *
	 * @param whole       整数部分，例如 '一又七分之三' 中的 一
	 * @param numerator   分子，例如 '一又七分之三' 中的 三
	 * @param denominator 分母，例如 '一又七分之三' 中的 七
	 * @return 一个新的分数实例
	 * @throws ArithmeticException 如果分母为 {@code zero}
	 * @throws ArithmeticException 如果分母为负数
	 * @throws ArithmeticException 如果分子为负数
	 * @throws ArithmeticException 如果结果分子超过 {@code Integer.MAX_VALUE}
	 */
	public static Fraction of(final int whole, final int numerator, final int denominator) {
		if (denominator == 0) {
			throw new ArithmeticException("The denominator must not be zero");
		}
		if (denominator < 0) {
			throw new ArithmeticException("The denominator must not be negative");
		}
		if (numerator < 0) {
			throw new ArithmeticException("The numerator must not be negative");
		}
		final long numeratorValue;
		if (whole < 0) {
			numeratorValue = whole * (long) denominator - numerator;
		} else {
			numeratorValue = whole * (long) denominator + numerator;
		}
		if (numeratorValue < Integer.MIN_VALUE || numeratorValue > Integer.MAX_VALUE) {
			throw new ArithmeticException("Numerator too large to represent as an Integer.");
		}
		return new Fraction((int) numeratorValue, denominator);
	}

	/**
	 * 从 {@link String} 创建一个 Fraction。
	 *
	 * <p>接受的格式有：</p>
	 *
	 * <ol>
	 *  <li>包含点的 {@code double} 字符串</li>
	 *  <li>'X Y/Z'</li>
	 *  <li>'Y/Z'</li>
	 *  <li>'X'（一个简单的整数）</li>
	 * </ol>
	 * <p>和一个 .。</p>
	 *
	 * @param str 要解析的字符串，必须不为 {@code null}
	 * @return 新的 {@code Fraction} 实例
	 * @throws NullPointerException  如果字符串为 {@code null}
	 * @throws NumberFormatException 如果数字格式无效
	 */
	public static Fraction of(String str) {
		Objects.requireNonNull(str, "str");
		// parse double format
		int pos = str.indexOf('.');
		if (pos >= 0) {
			return of(Double.parseDouble(str));
		}

		// parse X Y/Z format
		pos = str.indexOf(' ');
		if (pos > 0) {
			final int whole = Integer.parseInt(str.substring(0, pos));
			str = str.substring(pos + 1);
			pos = str.indexOf('/');
			if (pos < 0) {
				throw new NumberFormatException("The fraction could not be parsed as the format X Y/Z");
			}
			final int numer = Integer.parseInt(str.substring(0, pos));
			final int denom = Integer.parseInt(str.substring(pos + 1));
			return of(whole, numer, denom);
		}

		// parse Y/Z format
		pos = str.indexOf('/');
		if (pos < 0) {
			// simple whole number
			return of(Integer.parseInt(str), 1);
		}
		final int numer = Integer.parseInt(str.substring(0, pos));
		final int denom = Integer.parseInt(str.substring(pos + 1));
		return of(numer, denom);
	}

	/**
	 * 使用分数的两个部分 Y/Z 创建一个简化后的 {@code Fraction} 实例。
	 *
	 * <p>例如，如果输入参数表示 2/4，则创建的分数将是 1/2。</p>
	 *
	 * <p>任何负号都会被解析到分子上。</p>
	 *
	 * @param numerator   分子，例如 '3/7' 中的 3
	 * @param denominator 分母，例如 '3/7' 中的 7
	 * @return 一个新的分数实例，分子和分母已简化
	 * @throws ArithmeticException 如果分母为 {@code zero}
	 */
	public static Fraction ofReduced(int numerator, int denominator) {
		if (denominator == 0) {
			throw new ArithmeticException("The denominator must not be zero");
		}
		if (numerator == 0) {
			return ZERO; // normalize zero.
		}
		// allow 2^k/-2^31 as a valid fraction (where k>0)
		if (denominator == Integer.MIN_VALUE && (numerator & 1) == 0) {
			numerator /= 2;
			denominator /= 2;
		}
		if (denominator < 0) {
			if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
				throw new ArithmeticException("overflow: can't negate");
			}
			numerator = -numerator;
			denominator = -denominator;
		}
		// simplify fraction.
		final int gcd = MathUtil.gcd(numerator, denominator);
		numerator /= gcd;
		denominator /= gcd;
		return new Fraction(numerator, denominator);
	}

	/**
	 * 分子数部分（三个七分之三的 3）。
	 */
	private final int numerator;
	/**
	 * 分母是分数的一部分（三个七分之一中的 7）。
	 */
	private final int denominator;
	/**
	 * 缓存输出 hashCode（类是不可变的）。
	 */
	private transient int hashCode;
	/**
	 * 缓存输出 toString（类是不可变的）。
	 */
	private transient String toString;
	/**
	 * 缓存输出到 ProperString（类是不可变的）。
	 */
	private transient String toProperString;

	/**
	 * 使用分数的两个部分构造一个 {@code Fraction} 实例，例如 Y/Z。
	 *
	 * @param numerator   分子，例如在“七分之三”中的三
	 * @param denominator 分母，例如在“七分之三”中的七
	 */
	public Fraction(final int numerator, final int denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}

	/**
	 * 获取与此分数等值的正分数。
	 * <p>更精确地说：{@code (fraction >= 0 ? this : -fraction)}</p>
	 * <p>返回的分数不会被约简。</p>
	 *
	 * @return 如果此分数为正，则返回 {@code this}；否则返回一个新的正分数实例，其分子符号相反
	 */
	public Fraction abs() {
		if (numerator >= 0) {
			return this;
		}
		return negate();
	}

	/**
	 * 将此分数与另一个分数相加，并返回约简后的结果。
	 * 该算法遵循 Knuth 的 4.5.1 节。
	 *
	 * @param fraction 要添加的分数，不能为空
	 * @return 包含结果值的 {@code Fraction} 实例
	 * @throws NullPointerException 如果传入的分数为 {@code null}
	 * @throws ArithmeticException  如果结果的分子或分母超出 {@code Integer.MAX_VALUE}
	 */
	public Fraction add(final Fraction fraction) {
		return addSub(fraction, true /* add */);
	}

	/**
	 * 使用 Knuth 4.5.1 节中描述的算法实现加法和减法。
	 *
	 * @param fraction 要加或减的分数，不能为空
	 * @param isAdd    如果为 true，则执行加法；如果为 false，则执行减法
	 * @return 包含结果值的 {@code Fraction} 实例
	 * @throws IllegalArgumentException 如果传入的分数为 {@code null}
	 * @throws ArithmeticException      如果结果的分子或分母无法表示为 {@code int}
	 */
	private Fraction addSub(final Fraction fraction, final boolean isAdd) {
		Objects.requireNonNull(fraction, "fraction");
		// zero is identity for addition.
		if (numerator == 0) {
			return isAdd ? fraction : fraction.negate();
		}
		if (fraction.numerator == 0) {
			return this;
		}
		// if denominators are randomly distributed, d1 will be 1 about 61%
		// of the time.
		final int d1 = MathUtil.gcd(denominator, fraction.denominator);
		if (d1 == 1) {
			// result is ( (u*v' +/- u'v) / u'v')
			final int uvp = mulAndCheck(numerator, fraction.denominator);
			final int upv = mulAndCheck(fraction.numerator, denominator);
			return new Fraction(isAdd ? addAndCheck(uvp, upv) : subAndCheck(uvp, upv), mulPosAndCheck(denominator,
				fraction.denominator));
		}
		// the quantity 't' requires 65 bits of precision; see knuth 4.5.1
		// exercise 7. we're going to use a BigInteger.
		// t = u(v'/d1) +/- v(u'/d1)
		final BigInteger uvp = BigInteger.valueOf(numerator).multiply(BigInteger.valueOf(fraction.denominator / d1));
		final BigInteger upv = BigInteger.valueOf(fraction.numerator).multiply(BigInteger.valueOf(denominator / d1));
		final BigInteger t = isAdd ? uvp.add(upv) : uvp.subtract(upv);
		// but d2 doesn't need extra precision because
		// d2 = gcd(t,d1) = gcd(t mod d1, d1)
		final int tmodd1 = t.mod(BigInteger.valueOf(d1)).intValue();
		final int d2 = tmodd1 == 0 ? d1 : MathUtil.gcd(tmodd1, d1);

		// result is (t/d2) / (u'/d1)(v'/d2)
		final BigInteger w = t.divide(BigInteger.valueOf(d2));
		if (w.bitLength() > 31) {
			throw new ArithmeticException("overflow: numerator too large after multiply");
		}
		return new Fraction(w.intValue(), mulPosAndCheck(denominator / d1, fraction.denominator / d2));
	}

	/**
	 * 将此分数除以另一个分数。
	 *
	 * @param fraction 要除以的分数，不能为空
	 * @return 包含结果值的 {@code Fraction} 实例
	 * @throws NullPointerException 如果传入的分数为 {@code null}
	 * @throws ArithmeticException  如果要除以的分数为零
	 * @throws ArithmeticException  如果结果的分子或分母超出 {@code Integer.MAX_VALUE}
	 */
	public Fraction divideBy(final Fraction fraction) {
		Objects.requireNonNull(fraction, "fraction");
		if (fraction.numerator == 0) {
			throw new ArithmeticException("The fraction to divide by must not be zero");
		}
		return multiplyBy(fraction.invert());
	}

	/**
	 * 获取分数的分母部分。
	 *
	 * @return 分数的分母部分
	 */
	public int getDenominator() {
		return denominator;
	}

	/**
	 * 获取分数的分子部分。
	 *
	 * <p>此方法可能返回一个大于分母的值，即一个假分数，例如 7/4 中的七。</p>
	 *
	 * @return 分数的分子部分
	 */
	public int getNumerator() {
		return numerator;
	}

	/**
	 * 获取真分数的分子部分，始终为正数。
	 *
	 * <p>一个假分数 7/4 可以分解为真分数 1 3/4。
	 * 此方法返回真分数中的 3。</p>
	 *
	 * <p>如果分数为负数，例如 -7/4，可以分解为 -1 3/4，
	 * 因此此方法返回正数的真分数分子，即 3。</p>
	 *
	 * @return 真分数的分子部分，始终为正数
	 */
	public int getProperNumerator() {
		return Math.abs(numerator % denominator);
	}

	/**
	 * 获取真分数的整数部分，包括符号。
	 *
	 * <p>一个假分数 7/4 可以分解为真分数 1 3/4。
	 * 此方法返回真分数中的 1。</p>
	 *
	 * <p>如果分数为负数，例如 -7/4，可以分解为 -1 3/4，
	 * 因此此方法返回真分数的整数部分 -1。</p>
	 *
	 * @return 真分数的整数部分，包括符号
	 */
	public int getProperWhole() {
		return numerator / denominator;
	}

	/**
	 * 获取此分数的倒数 (1/fraction)。
	 *
	 * <p>返回的分数不会被约简。</p>
	 *
	 * @return 一个新分数实例，其分子和分母互换
	 * @throws ArithmeticException 如果分数表示零
	 */
	public Fraction invert() {
		if (numerator == 0) {
			throw new ArithmeticException("Unable to invert zero.");
		}
		if (numerator == Integer.MIN_VALUE) {
			throw new ArithmeticException("overflow: can't negate numerator");
		}
		if (numerator < 0) {
			return new Fraction(-denominator, -numerator);
		}
		return new Fraction(denominator, numerator);
	}

	/**
	 * 将此分数与另一个分数相乘，并返回约简后的结果。
	 *
	 * @param fraction 要乘以的分数，不能为空
	 * @return 包含结果值的 {@code Fraction} 实例
	 * @throws NullPointerException 如果传入的分数为 {@code null}
	 * @throws ArithmeticException  如果结果的分子或分母超出 {@code Integer.MAX_VALUE}
	 */
	public Fraction multiplyBy(final Fraction fraction) {
		Objects.requireNonNull(fraction, "fraction");
		if (numerator == 0 || fraction.numerator == 0) {
			return ZERO;
		}
		// knuth 4.5.1
		// make sure we don't overflow unless the result *must* overflow.
		final int d1 = MathUtil.gcd(numerator, fraction.denominator);
		final int d2 = MathUtil.gcd(fraction.numerator, denominator);
		return ofReduced(mulAndCheck(numerator / d1, fraction.numerator / d2), mulPosAndCheck(denominator / d2, fraction.denominator / d1));
	}

	/**
	 * 获取此分数的负数 (-fraction)。
	 *
	 * <p>返回的分数不会被约简。</p>
	 *
	 * @return 一个新分数实例，其分子符号相反
	 */
	public Fraction negate() {
		// the positive range is one smaller than the negative range of an int.
		if (numerator == Integer.MIN_VALUE) {
			throw new ArithmeticException("overflow: too large to negate");
		}
		return new Fraction(-numerator, denominator);
	}

	/**
	 * 获取此分数的指定幂次。
	 *
	 * <p>返回的分数已约简。</p>
	 *
	 * @param power 要将分数提升到的幂次
	 * @return 如果幂次为一，则返回 {@code this}；如果幂次为零（即使分数等于零），则返回 {@link #ONE}；
	 * 否则返回一个新的分数实例，提升到相应的幂次
	 * @throws ArithmeticException 如果结果的分子或分母超出 {@code Integer.MAX_VALUE}
	 */
	public Fraction pow(final int power) {
		if (power == 1) {
			return this;
		}
		if (power == 0) {
			return ONE;
		}
		if (power < 0) {
			if (power == Integer.MIN_VALUE) { // MIN_VALUE can't be negated.
				return this.invert().pow(2).pow(-(power / 2));
			}
			return this.invert().pow(-power);
		}
		final Fraction f = this.multiplyBy(this);
		if (power % 2 == 0) { // if even...
			return f.pow(power / 2);
		}
		return f.pow(power / 2).multiplyBy(this);
	}

	/**
	 * 将分数约简为分子和分母的最小值，并返回结果。
	 *
	 * <p>例如，如果此分数表示 2/4，则结果将是 1/2。</p>
	 *
	 * @return 一个新的约简后的分数实例，如果无法进一步简化，则返回此实例
	 */
	public Fraction reduce() {
		if (numerator == 0) {
			return equals(ZERO) ? this : ZERO;
		}
		final int gcd = MathUtil.gcd(Math.abs(numerator), denominator);
		if (gcd == 1) {
			return this;
		}
		return of(numerator / gcd, denominator / gcd);
	}

	/**
	 * 从此分数中减去另一个分数的值，并返回约简后的结果。
	 *
	 * @param fraction 要减去的分数，不能为空
	 * @return 包含结果值的 {@code Fraction} 实例
	 * @throws NullPointerException 如果传入的分数为 {@code null}
	 * @throws ArithmeticException  如果结果的分子或分母无法表示为 {@code int}
	 */
	public Fraction subtract(final Fraction fraction) {
		return addSub(fraction, false /* subtract */);
	}

	/**
	 * 获取此分数的真分数形式的字符串表示，格式为 X Y/Z。
	 *
	 * <p>使用的格式为 '<em>整数部分</em> <em>分子</em>/<em>分母</em>'。
	 * 如果整数部分为零，则省略整数部分。如果分子为零，则仅返回整数部分。</p>
	 *
	 * @return 分数的字符串表示形式
	 */
	public String toProperString() {
		if (toProperString == null) {
			if (numerator == 0) {
				toProperString = "0";
			} else if (numerator == denominator) {
				toProperString = "1";
			} else if (numerator == -1 * denominator) {
				toProperString = "-1";
			} else if ((numerator > 0 ? -numerator : numerator) < -denominator) {
				// note that we do the magnitude comparison test above with
				// NEGATIVE (not positive) numbers, since negative numbers
				// have a larger range. otherwise numerator == Integer.MIN_VALUE
				// is handled incorrectly.
				final int properNumerator = getProperNumerator();
				if (properNumerator == 0) {
					toProperString = Integer.toString(getProperWhole());
				} else {
					toProperString = getProperWhole() + " " + properNumerator + "/" + getDenominator();
				}
			} else {
				toProperString = getNumerator() + "/" + getDenominator();
			}
		}
		return toProperString;
	}

	@Override
	public double doubleValue() {
		return (double) numerator / (double) denominator;
	}

	@Override
	public float floatValue() {
		return (float) numerator / (float) denominator;
	}

	@Override
	public int intValue() {
		return numerator / denominator;
	}

	@Override
	public long longValue() {
		return (long) numerator / denominator;
	}

	@Override
	public int compareTo(final Fraction other) {
		if (equals(other)) {
			return 0;
		}

		// otherwise see which is less
		final long first = (long) numerator * (long) other.denominator;
		final long second = (long) other.numerator * (long) denominator;
		return Long.compare(first, second);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Fraction other)) {
			return false;
		}
		return getNumerator() == other.getNumerator() && getDenominator() == other.getDenominator();
	}

	@Override
	public int hashCode() {
		if (hashCode == 0) {
			// hash code update should be atomic.
			hashCode = 37 * (37 * 17 + getNumerator()) + getDenominator();
		}
		return hashCode;
	}

	@Override
	public String toString() {
		if (toString == null) {
			toString = getNumerator() + "/" + getDenominator();
		}
		return toString;
	}

	// region ----- private methods

	/**
	 * 添加两个整数，检查溢出。
	 *
	 * @param x 加数
	 * @param y 加数
	 * @return 和 {@code x+y}
	 * @throws ArithmeticException 如果结果不能表示为 int
	 */
	private static int addAndCheck(final int x, final int y) {
		final long s = (long) x + (long) y;
		if (s < Integer.MIN_VALUE || s > Integer.MAX_VALUE) {
			throw new ArithmeticException("overflow: add");
		}
		return (int) s;
	}

	/**
	 * 乘以两个整数，并检查是否溢出。
	 *
	 * @param x 一个乘数
	 * @param y 另一个乘数
	 * @return 乘积 {@code x*y}
	 * @throws ArithmeticException 如果结果不能表示为 int 类型
	 */
	private static int mulAndCheck(final int x, final int y) {
		final long m = (long) x * (long) y;
		if (m < Integer.MIN_VALUE || m > Integer.MAX_VALUE) {
			throw new ArithmeticException("overflow: mul");
		}
		return (int) m;
	}

	/**
	 * 乘以两个正整数，并检查是否溢出。
	 *
	 * @param x 一个正乘数
	 * @param y 另一个正乘数
	 * @return 乘积 {@code x*y}
	 * @throws ArithmeticException 如果结果不能表示为 int 类型 或者任意一个参数不是正数
	 */
	private static int mulPosAndCheck(final int x, final int y) {
		/* assert x>=0 && y>=0; */
		final long m = (long) x * (long) y;
		if (m > Integer.MAX_VALUE) {
			throw new ArithmeticException("overflow: mulPos");
		}
		return (int) m;
	}

	/**
	 * 从一个整数中减去另一个整数，并检查是否溢出。
	 *
	 * @param x 被减数
	 * @param y 减数
	 * @return 差值 {@code x - y}
	 * @throws ArithmeticException 如果结果不能表示为 int 类型
	 */
	private static int subAndCheck(final int x, final int y) {
		final long s = (long) x - (long) y;
		if (s < Integer.MIN_VALUE || s > Integer.MAX_VALUE) {
			throw new ArithmeticException("overflow: add");
		}
		return (int) s;
	}
	// endregion
}

