package cn.hutool.core.math;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Stack;

/**
 * 数学表达式计算工具类<br>
 * 见：https://github.com/chinabugotech/hutool/issues/1090#issuecomment-693750140
 *
 * @author trainliang, looly
 * @since 5.4.3
 */
public class Calculator {
	private final Stack<String> postfixStack = new Stack<>();// 后缀式栈
	private final int[] operatPriority = new int[]{0, 3, 2, 1, -1, 1, 0, 2};// 运用运算符ASCII码-40做索引的运算符优先级

	/**
	 * 计算表达式的值
	 *
	 * @param expression 表达式
	 * @return 计算结果
	 */
	public static double conversion(String expression) {
		return (new Calculator()).calculate(expression);
	}

	/**
	 * 按照给定的表达式计算
	 *
	 * @param expression 要计算的表达式例如:5+12*(3+5)/7
	 * @return 计算结果
	 */
	public double calculate(String expression) {
		prepare(transform(expression));

		final Stack<String> resultStack = new Stack<>();
		Collections.reverse(postfixStack);// 将后缀式栈反转
		String firstValue, secondValue, currentOp;// 参与计算的第一个值，第二个值和算术运算符
		while (false == postfixStack.isEmpty()) {
			currentOp = postfixStack.pop();
			if (false == isOperator(currentOp.charAt(0))) {// 如果不是运算符则存入操作数栈中
				currentOp = currentOp.replace("~", "-");
				resultStack.push(currentOp);
			} else {// 如果是运算符则从操作数栈中取两个值和该数值一起参与运算
				secondValue = resultStack.pop();
				firstValue = resultStack.pop();

				// 将负数标记符改为负号
				firstValue = firstValue.replace("~", "-");
				secondValue = secondValue.replace("~", "-");

				final BigDecimal tempResult = calculate(firstValue, secondValue, currentOp.charAt(0));
				resultStack.push(tempResult.toString());
			}
		}

		// 当结果集中有多个数字时，可能是省略*，类似(1+2)3
		return NumberUtil.mul(resultStack.toArray(new String[0])).doubleValue();
		//return Double.parseDouble(resultStack.pop());
	}

	/**
	 * 数据准备阶段将表达式转换成为后缀式栈
	 *
	 * @param expression 表达式
	 */
	private void prepare(String expression) {
		final Stack<Character> opStack = new Stack<>();
		opStack.push(',');// 运算符放入栈底元素逗号，此符号优先级最低
		final char[] arr = expression.toCharArray();
		int currentIndex = 0;// 当前字符的位置
		int count = 0;// 上次算术运算符到本次算术运算符的字符的长度便于或者之间的数值
		char currentOp, peekOp;// 当前操作符和栈顶操作符
		for (int i = 0; i < arr.length; i++) {
			currentOp = arr[i];
			if (isOperator(currentOp)) {// 如果当前字符是运算符
				if (count > 0) {
					postfixStack.push(new String(arr, currentIndex, count));// 取两个运算符之间的数字
				}
				peekOp = opStack.peek();
				if (currentOp == ')') {// 遇到反括号则将运算符栈中的元素移除到后缀式栈中直到遇到左括号
					while (opStack.peek() != '(') {
						postfixStack.push(String.valueOf(opStack.pop()));
					}
					opStack.pop();
				} else {
					while (currentOp != '(' && peekOp != ',' && compare(currentOp, peekOp)) {
						postfixStack.push(String.valueOf(opStack.pop()));
						peekOp = opStack.peek();
					}
					opStack.push(currentOp);
				}
				count = 0;
				currentIndex = i + 1;
			} else {
				count++;
			}
		}
		//新增防止数组越界
		if (count > 1 || (count == 1 && currentIndex < arr.length && !isOperator(arr[currentIndex]))) {// 最后一个字符不是括号或者其他运算符的则加入后缀式栈中
			postfixStack.push(new String(arr, currentIndex, count));
		}

		while (opStack.peek() != ',') {
			postfixStack.push(String.valueOf(opStack.pop()));// 将操作符栈中的剩余的元素添加到后缀式栈中
		}
	}

	/**
	 * 判断是否为算术符号
	 *
	 * @param c 字符
	 * @return 是否为算术符号
	 */
	private boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')' || c == '%';
	}

	/**
	 * 利用ASCII码-40做下标去算术符号优先级
	 *
	 * @param cur  下标
	 * @param peek peek
	 * @return 优先级，如果cur高或相等，返回true，否则false
	 */
	private boolean compare(char cur, char peek) {// 如果是peek优先级高于cur，返回true，默认都是peek优先级要低
		final int offset = 40;
		if(cur  == '%'){
			// %优先级最高
			cur = 47;
		}
		if(peek  == '%'){
			// %优先级最高
			peek = 47;
		}

		return operatPriority[peek - offset] >= operatPriority[cur - offset];
	}

	/**
	 * 按照给定的算术运算符做计算
	 *
	 * @param firstValue  第一个值
	 * @param secondValue 第二个值
	 * @param currentOp   算数符，只支持'+'、'-'、'*'、'/'、'%'
	 * @return 结果
	 */
	private BigDecimal calculate(String firstValue, String secondValue, char currentOp) {
		final BigDecimal first = NumberUtil.toBigDecimal(firstValue);
		final BigDecimal second = NumberUtil.toBigDecimal(secondValue);

		//添加除零检查并提供明确错误信息
		if ((currentOp == '/' || currentOp == '%') && second.compareTo(BigDecimal.ZERO) == 0) {
			throw new ArithmeticException("Division by zero: cannot divide " + firstValue + " by zero");
		}

		final BigDecimal result;
		switch (currentOp) {
			case '+':
				result = NumberUtil.add(firstValue, secondValue);
				break;
			case '-':
				result = NumberUtil.sub(firstValue, secondValue);
				break;
			case '*':
				result = NumberUtil.mul(firstValue, secondValue);
				break;
			case '/':
				result = NumberUtil.div(firstValue, secondValue);
				break;
			case '%':
				result = NumberUtil.toBigDecimal(firstValue).remainder(NumberUtil.toBigDecimal(secondValue));
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + currentOp);
		}
		return result;
	}

	/**
	 * 将表达式中的一元负号转换为内部标记（~），便于后续解析。
	 * 规则说明：
	 *  - 科学计数法整体识别为数字，e/E 后的 + 或 - 属于指数符号，不参与一元符号折叠。
	 *  - 一元 + / - 仅在表达式开头或运算符、左括号之后生效；可折叠连续符号，如 --3、+-3 -> ~3。
	 * 示例：
	 *  - 输入：-2+-1*(-3E-2)-(-1)
	 *  - 输出：~2+~1*(~3E~2)-(~1)
	 */
	private static String transform(String expression) {
		expression = StrUtil.cleanBlank(expression);
		expression = StrUtil.removeSuffix(expression, "=");
		final char[] arr = expression.toCharArray();

		final StringBuilder out = new StringBuilder(arr.length);
		for (int i = 0; i < arr.length; i++) {
			char c = arr[i];

			// 把x或X当作 *
			if (CharUtil.equals(c, 'x', true)) {
				out.append('*');
				continue;
			}

			// 若是'+'或'-'，需要判断是指数符号、二元运算符还是一元运算符序列
			if (c == '+' || c == '-') {
				// 如果前一个已写入的字符为'e'或'E'，则视作科学计数法的符号
				int outLen = out.length();
				if (outLen > 0) {
					char prevOut = out.charAt(outLen - 1);
					if (prevOut == 'e' || prevOut == 'E') {
						// 在e/E 后：
						// '+' 可以安全丢弃（1e+3 == 1e3）
						// '-' 必须保留但不能被当作二元运算符，故用'~'临时替代，后续再还原为'-'
						if (c == '-') {
							out.append('~');
						}
						continue;
					}
				}

				// 查找前一个非空字符（原串中的），用于判断是否为一元上下文
				int j = i - 1;
				while (j >= 0 && Character.isWhitespace(arr[j])) j--;
				boolean unaryContext = (j < 0) || isPrevCharOperatorOrLeftParen(arr[j]);

				if (unaryContext) {
					// 收集连续的一系列 + 或 -（例如 --+ - -> 合并为一个净符号）
					int k = i;
					int minusCount = 0;
					while (k < arr.length && (arr[k] == '+' || arr[k] == '-')) {
						if (arr[k] == '-') minusCount++;
						k++;
					}
					boolean netNegative = (minusCount % 2 == 1);
					if (netNegative) {
						// 用~标记一元负号（与原实现保持兼容）
						out.append('~');
					}
					i = k - 1;
				} else {
					//二元运算符，直接写入 + 或 -
					out.append(c);
				}
				continue;
			}
			//其它字符（包括数字、字母、括号、e、E、小数点等）直接追加
			out.append(c);
		}

		// 特殊处理：如果开头为 "~("，原实现会将其转为 "0~(" 形式改为以0开始的负括号处理
		final String result = out.toString();
		final char[] resArr = result.toCharArray();
		if (resArr.length >= 2 && resArr[0] == '~' && resArr[1] == '(') {
			resArr[0] = '-';
			return "0" + new String(resArr);
		} else {
			return result;
		}
	}

	/**
	 * 判断给定位置前一个非空字符是否为运算符或左括号（用于判定是否为一元上下文）
	 */
	private static boolean isPrevCharOperatorOrLeftParen(char c) {
		return c == '%' || c == '+' || c == '-' || c == '*' || c == '/' || c == '(';
	}
}
