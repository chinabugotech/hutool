/*
 * Copyright (c) 2026 Hutool Team.
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

package cn.hutool.v7.extra.validation;

import cn.hutool.v7.core.array.ArrayUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ValidationUtil 单元测试
 *
 * @author Zulu
 * @since 7.0.0
 */
public class ValidationUtilTest {

	@SuppressWarnings("EqualsWithItself")
	@Test
	void getValidatorTest() {
		// 测试获取默认验证器
		assertNotNull(ValidationUtil.getValidator());
		assertSame(ValidationUtil.getValidator(), ValidationUtil.getValidator());
	}

	@Test
	void validateTest() {
		// 创建需要校验的对象
		final User user = new User();
		user.setUsername("admin");
		user.setPassword("123456"); // 密码不符合规则
		user.setEmail("invalid-email"); // 邮箱格式不正确
		user.setAge(15); // 年龄不符合规则

		// 校验对象
		final Set<ConstraintViolation<User>> violations = ValidationUtil.validate(user);
		assertEquals(4, violations.size());

		final String[] errorMessages = {"ID不能为空", "密码必须包含大小写字母和数字，长度至少8位", "邮箱格式不正确", "年龄必须大于等于18岁"};
		violations.forEach(violation -> {
			assertTrue(ArrayUtil.contains(errorMessages, violation.getMessage()));
		});
	}

	@Test
	void validateWithValidObjectTest() {
		// 测试有效的对象
		final User user = new User();
		user.setId(1L);
		user.setUsername("validuser");
		user.setPassword("ValidPass123");
		user.setEmail("valid@example.com");
		user.setAge(25);

		final Set<ConstraintViolation<User>> violations = ValidationUtil.validate(user);
		assertTrue(violations.isEmpty());
	}

	@Test
	void validateWithEmptyGroupsTest() {
		// 测试空分组
		final User user = new User();
		user.setUsername("admin");

		final Set<ConstraintViolation<User>> violations = ValidationUtil.validate(user);
		assertFalse(violations.isEmpty());
	}

	@Test
	void validateAndThrowFirstTest() {
		final User user = new User();
		user.setUsername("admin");
		user.setPassword("123456"); // 密码不符合规则
		assertThrows(ValidationException.class, () -> ValidationUtil.validateAndThrowFirst(user));
	}

	@Test
	void validateAndThrowFirstWithValidObjectTest() {
		// 测试有效对象不应该抛出异常
		final User user = new User();
		user.setId(1L);
		user.setUsername("validuser");
		user.setPassword("ValidPass123");
		user.setEmail("valid@example.com");
		user.setAge(25);

		assertDoesNotThrow(() -> ValidationUtil.validateAndThrowFirst(user));
	}

	@Test
	void validateAndThrowFirstExceptionMessageTest() {
		final User user = new User();
		user.setUsername("admin");
		user.setPassword("123456");

		final ValidationException exception = assertThrows(ValidationException.class,
			() -> ValidationUtil.validateAndThrowFirst(user));

		assertTrue(exception.getMessage().contains("ID不能为空") ||
			exception.getMessage().contains("密码必须包含大小写字母和数字，长度至少8位"));
	}

	@Test
	void validatePropertyTest() {
		final User user = new User();
		user.setUsername("admin"); // 有效的用户名
		user.setPassword("123456"); // 无效的密码

		// 测试有效属性
		Set<ConstraintViolation<User>> usernameViolations = ValidationUtil.validateProperty(user, "username");
		assertTrue(usernameViolations.isEmpty());

		// 测试无效属性
		Set<ConstraintViolation<User>> passwordViolations = ValidationUtil.validateProperty(user, "password");
		assertEquals(1, passwordViolations.size());
		assertEquals("密码必须包含大小写字母和数字，长度至少8位", passwordViolations.iterator().next().getMessage());
	}

	@Test
	void validatePropertyWithNonExistentPropertyTest() {
		final User user = new User();
		user.setUsername("admin");

		// 测试不存在的属性
		assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateProperty(user, "nonexistent"));
	}

	@Test
	void validatePropertyWithNullBeanTest() {
		// 测试空对象
		assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateProperty(null, "username"));
	}

	@Test
	void warpValidateTest() {
		final User user = new User();
		user.setUsername("admin");
		user.setPassword("123456"); // 密码不符合规则
		final BeanValidationResult result = ValidationUtil.warpValidate(user);
		assertFalse(result.isSuccess());
		assertEquals(2, result.getErrorMessages().size());

		result.getErrorMessages().forEach(errorMessage -> {
			assertTrue(
				errorMessage.getMessage().equals("密码必须包含大小写字母和数字，长度至少8位") ||
					errorMessage.getMessage().equals("ID不能为空"));
		});
	}

	@Test
	void warpValidateWithValidObjectTest() {
		final User user = new User();
		user.setId(1L);
		user.setUsername("validuser");
		user.setPassword("ValidPass123");
		user.setEmail("valid@example.com");
		user.setAge(25);

		final BeanValidationResult result = ValidationUtil.warpValidate(user);
		assertTrue(result.isSuccess());
		assertTrue(result.getErrorMessages().isEmpty());
	}

	@Test
	void warpValidatePropertyTest() {
		final User user = new User();
		user.setUsername("admin");
		user.setPassword("123456"); // 密码不符合规则

		final BeanValidationResult result = ValidationUtil.warpValidateProperty(user, "password");
		assertFalse(result.isSuccess());
		assertEquals(1, result.getErrorMessages().size());
		assertEquals("password", result.getErrorMessages().get(0).getPropertyName());
		assertEquals("密码必须包含大小写字母和数字，长度至少8位", result.getErrorMessages().get(0).getMessage());
	}

	@Test
	void warpValidatePropertyWithValidPropertyTest() {
		final User user = new User();
		user.setUsername("validuser");

		final BeanValidationResult result = ValidationUtil.warpValidateProperty(user, "username");
		assertTrue(result.isSuccess());
		assertTrue(result.getErrorMessages().isEmpty());
	}

	@Test
	void warpValidatePropertyWithNonExistentPropertyTest() {
		final User user = new User();
		user.setUsername("admin");

		assertThrows(IllegalArgumentException.class, ()->ValidationUtil.warpValidateProperty(user, "nonExistentProperty"));
	}

	@Test
	void warpBeanValidationResultTest() {
		final User user = new User();
		user.setUsername("admin");

		final Set<ConstraintViolation<User>> violations = ValidationUtil.validate(user);
		final BeanValidationResult result = new BeanValidationResult(violations.isEmpty());

		// 手动测试包装逻辑
		for (final ConstraintViolation<User> violation : violations) {
			final BeanValidationResult.ErrorMessage errorMessage = new BeanValidationResult.ErrorMessage();
			errorMessage.setPropertyName(violation.getPropertyPath().toString());
			errorMessage.setMessage(violation.getMessage());
			errorMessage.setValue(violation.getInvalidValue());
			result.addErrorMessage(errorMessage);
		}

		assertFalse(result.isSuccess());
		assertFalse(result.getErrorMessages().isEmpty());
	}

	@Test
	void nestedValidationTest() {
		final Company company = new Company();
		final User user = new User();
		user.setUsername("admin");
		user.setPassword("123456");
		company.setAdmin(user);

		final Set<ConstraintViolation<Company>> violations = ValidationUtil.validate(company);
		assertFalse(violations.isEmpty());
	}

	@Test
	void boundaryValuesTest() {
		final User user = new User();

		// 测试边界值 - 用户名长度
		user.setUsername("a"); // 小于最小值
		Set<ConstraintViolation<User>> violations = ValidationUtil.validate(user);
		assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("用户名长度必须在2-20个字符之间")));

		user.setUsername("verylongusernameexceedingtwentycharacters"); // 超过最大值
		violations = ValidationUtil.validate(user);
		assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("用户名长度必须在2-20个字符之间")));

		// 测试边界值 - 年龄
		user.setUsername("testuser");
		user.setAge(17); // 小于最小值
		violations = ValidationUtil.validate(user);
		assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("年龄必须大于等于18岁")));

		user.setAge(101); // 超过最大值
		violations = ValidationUtil.validate(user);
		assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("年龄必须小于等于100岁")));
	}

	@Test
	void nullAndEmptyValuesTest() {
		final User user = new User();

		// 测试空用户名
		user.setUsername("");
		Set<ConstraintViolation<User>> violations = ValidationUtil.validate(user);
		assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("用户名不能为空")));

		// 测试 null 用户名
		user.setUsername(null);
		violations = ValidationUtil.validate(user);
		assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("用户名不能为空")));

		// 测试 null ID
		user.setId(null);
		violations = ValidationUtil.validate(user);
		assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("ID不能为空")));
	}

	@Test
	void validationGroupsTest() {
		final UserWithGroups user = new UserWithGroups();
		user.setUsername("test");
		user.setPassword("SimplePass1");

		// 测试默认分组（应该验证）
		Set<ConstraintViolation<UserWithGroups>> violations = ValidationUtil.validate(user);
		assertTrue(violations.isEmpty());

		// 测试创建分组（应该验证）
		violations = ValidationUtil.validate(user, CreateGroup.class);
		assertTrue(violations.isEmpty());

		// 测试更新分组（密码应该验证失败）
		violations = ValidationUtil.validate(user, UpdateGroup.class);
		assertEquals(1, violations.size());
		assertEquals("更新时密码必须包含特殊字符", violations.iterator().next().getMessage());
	}

	@Data
	static class User {
		@NotNull(message = "ID不能为空")
		private Long id;

		@NotBlank(message = "用户名不能为空")
		@Size(min = 2, max = 20, message = "用户名长度必须在2-20个字符之间")
		private String username;

		@NotBlank(message = "密码不能为空")
		@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "密码必须包含大小写字母和数字，长度至少8位")
		private String password;

		@Email(message = "邮箱格式不正确")
		private String email;

		@Min(value = 18, message = "年龄必须大于等于18岁")
		@Max(value = 100, message = "年龄必须小于等于100岁")
		private Integer age;
	}

	@Data
	static class Company {
		@NotNull(message = "公司名称不能为空")
		private String name;

		@Valid
		private User admin;
	}

	interface CreateGroup {
	}

	interface UpdateGroup {
	}

	@Data
	static class UserWithGroups {
		@NotBlank(message = "用户名不能为空", groups = {CreateGroup.class, UpdateGroup.class})
		@Size(min = 2, max = 20, message = "用户名长度必须在2-20个字符之间", groups = {CreateGroup.class, UpdateGroup.class})
		private String username;

		@NotBlank(message = "密码不能为空", groups = CreateGroup.class)
		@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
			message = "密码必须包含大小写字母和数字，长度至少8位", groups = CreateGroup.class)
		@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
			message = "更新时密码必须包含特殊字符", groups = UpdateGroup.class)
		private String password;
	}
}
