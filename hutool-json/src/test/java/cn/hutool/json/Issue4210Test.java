package cn.hutool.json;

import cn.hutool.core.exceptions.InvocationTargetRuntimeException;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.ObjectId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

public class Issue4210Test {

	@Test
	void setValueTest(){
		final JSONObject jsonObject = new JSONObject();

		final TestEntity entity = new TestEntity("张三", "社畜");
		Assertions.assertThrows(InvocationTargetRuntimeException.class, () -> jsonObject.set("entity",entity));
	}

	@Data
	public static class BaseEntity implements Serializable {

		/**
		 * 实体唯一标识符
		 */
		private String _id;
		private ObjectId id;

		public String get_id() {
			return _id == null ? id.toString() : _id;
		}
	}

	@EqualsAndHashCode(callSuper = true)
	@Data
	@AllArgsConstructor
	public class TestEntity extends BaseEntity{
		private String name;
		private String desc;
	}
}
