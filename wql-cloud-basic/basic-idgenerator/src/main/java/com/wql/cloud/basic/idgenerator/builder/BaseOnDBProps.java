package com.wql.cloud.basic.idgenerator.builder;

public class BaseOnDBProps {

	private static final String TABLE_NAME = "t_id_gen";

	public BaseOnDBProps(String project, String model) {
		super();
		this.project = project;
		this.model = model;
	}

	public BaseOnDBProps(String project, String model, String table) {
		super();
		this.project = project;
		this.model = model;
		this.table = table;
	}

	public BaseOnDBProps() {
		super();
	}

	@Override
	public String toString() {
		return "BaseOnDBProps [project=" + project + ", model=" + model + ", table=" + table + "]";
	}

	private String project;
	private String model;
	private String table;

	/**
	 * 属性构造器默认表名:t_id_gen
	 *
	 * @param project
	 * @param model
	 * @return
	 */
	public static BaseOnDBProps of(String project, String model) {
		return new BaseOnDBProps(project, model, TABLE_NAME);
	}

	public static BaseOnDBProps of(String project, String model, String table) {
		return new BaseOnDBProps(project, model, table);
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
}
