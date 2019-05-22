package com.wql.cloud.tool.dbencrypt.service;

import java.util.List;

import com.wql.cloud.tool.bean.BeanUtils;
import com.wql.cloud.tool.string.StringUtils;

public interface DBEncryptService {

	/**
	 * 数据库字段加密
	 *
	 * @param content
	 * @return
	 */
	String encrypt(String content);

	/**
	 * 数据库字段解密
	 *
	 * @param content
	 * @return
	 */
	String decrypt(String content);

	/**
	 * 获取密钥key
	 * 
	 * @return
	 */
	String getEncryptKey();

	/**
	 * 对象加密
	 *
	 * @param t
	 * @param fields get,set方法列表
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default <T> T encrypt(T t, Field<T>... fields) {
		if (fields == null || fields.length == 0 || t == null) {
			return t;
		}
		T copy = (T) BeanUtils.copy(t, t.getClass());
		for (Field<T> filed : fields) {
			String filedValue = filed.getter.get(t);
			if (StringUtils.isNotEmpty(filedValue)) {
				filed.setter.set(copy, encrypt(filedValue));
			}
		}
		return copy;
	}

	/**
	 * 对象集合加密
	 *
	 * @param tList
	 * @param fields get,set方法列表
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default <T> List<T> encrypt(List<T> tList, Field<T>... fields) {
		if (fields == null || fields.length == 0 || tList == null || tList.isEmpty()) {
			return tList;
		}
		List<?> copyList = BeanUtils.copyByList(tList, tList.get(0).getClass());
		tList.clear();
		for (Object obj : copyList) {
			T t = (T) obj;
			tList.add(encrypt(t, fields));
		}
		return tList;
	}

	/**
	 * 对象解密
	 *
	 * @param t
	 * @param fields get,set方法列表
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default <T> T decrypt(T t, Field<T>... fields) {
		if (fields == null || fields.length == 0 || t == null) {
			return t;
		}
		T copy = (T) BeanUtils.copy(t, t.getClass());
		for (Field<T> filed : fields) {
			String filedValue = filed.getter.get(t);
			if (StringUtils.isNotEmpty(filedValue)) {
				filed.setter.set(copy, decrypt(filedValue));
			}
		}
		return copy;
	}

	/**
	 * 集合解密
	 *
	 * @param tList
	 * @param fields get,set方法列表
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default <T> List<T> decrypt(List<T> tList, Field<T>... fields) {
		if (fields == null || fields.length == 0 || tList == null || tList.isEmpty()) {
			return tList;
		}
		List<?> copyList = BeanUtils.copyByList(tList, tList.get(0).getClass());
		tList.clear();
		for (Object obj : copyList) {
			T t = (T) obj;
			tList.add(decrypt(t, fields));
		}
		return tList;
	}

	interface Getter<T> {

		String get(T t);
	}

	interface Setter<T> {

		void set(T t, String r);
	}

	@SuppressWarnings("all")
	class Field<T> {
		private Getter<T> getter;
		private Setter<T> setter;
		public Field(Getter<T> getter, Setter<T> setter) {
			this.getter = getter;
			this.setter = setter;
		}

		public static <T> Field<T>[] of(Getter<T> g1, Setter<T> s1) {
			return new Field[] { new Field(g1, s1) };
		}

		public static <T> Field[] of(Getter<T> g1, Setter<T> s1, Getter<T> g2, Setter<T> s2) {
			return new Field[] { new Field(g1, s1), new Field(g2, s2) };
		}

		public static <T> Field[] of(Getter<T> g1, Setter<T> s1, Getter<T> g2, Setter<T> s2, Getter<T> g3,
				Setter<T> s3) {
			return new Field[] { new Field(g1, s1), new Field(g2, s2), new Field(g3, s3) };
		}

		public static <T> Field[] of(Getter<T> g1, Setter<T> s1, Getter<T> g2, Setter<T> s2, Getter<T> g3, Setter<T> s3,
				Getter<T> g4, Setter<T> s4) {
			return new Field[] { new Field(g1, s1), new Field(g2, s2), new Field(g3, s3), new Field(g4, s4) };
		}

		public static <T> Field[] of(Getter<T> g1, Setter<T> s1, Getter<T> g2, Setter<T> s2, Getter<T> g3, Setter<T> s3,
				Getter<T> g4, Setter<T> s4, Getter<T> g5, Setter<T> s5) {
			return new Field[] { new Field(g1, s1), new Field(g2, s2), new Field(g3, s3), new Field(g4, s4),
					new Field(g5, s5) };
		}
	}

}
