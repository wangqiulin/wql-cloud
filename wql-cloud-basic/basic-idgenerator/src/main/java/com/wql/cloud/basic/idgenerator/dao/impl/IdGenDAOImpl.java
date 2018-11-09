package com.wql.cloud.basic.idgenerator.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wql.cloud.basic.idgenerator.builder.BaseOnDBProps;
import com.wql.cloud.basic.idgenerator.dao.IdGenDAO;
import com.wql.cloud.basic.idgenerator.model.IdGenInfo;

@Repository
public class IdGenDAOImpl implements IdGenDAO {

	private static final String TABLE_NAME = ":TABLE";

	private String update = " update `"+TABLE_NAME+"` set last_issued = last_issued + :steps "
			+ " where project_name = :projectName and model_name = :modelName ";

	private String insert = " insert into `"+TABLE_NAME+"` (project_name, model_name) " + " values (:projectName, :modelName)";

	private String select = " SELECT * FROM `"+TABLE_NAME+"` "
			+ " where project_name = :projectName and model_name = :modelName ";

	private NamedParameterJdbcTemplate template;

	private BaseOnDBProps props;

	public void setTemplate(NamedParameterJdbcTemplate template) {
		this.template = template;
	}

	public void setProps(BaseOnDBProps props) {
		this.props = props;
	}

	@Override
	public int update(IdGenInfo criteria) {
		if (criteria == null) {
			return 0;
		}
		return template.update(buildSql(update,TABLE_NAME,props.getTable()), new MapSqlParameterSource((JSONObject) JSON.toJSON(criteria)));
	}

	@Override
	public IdGenInfo get(IdGenInfo criteria) {
		if (criteria == null) {
            return null;
        }
        MapSqlParameterSource params = new MapSqlParameterSource((JSONObject) JSON.toJSON(criteria));
        return template.query(buildSql(select,TABLE_NAME,props.getTable()), params, idGenExtractor);
	}

	@Override
	public int insert(IdGenInfo criteria) {
		if (criteria == null) {
            return 0;
        }
        return template.update(buildSql(insert,TABLE_NAME,props.getTable()), new MapSqlParameterSource((JSONObject) JSON.toJSON(criteria)));
	}

	public String buildSql(String sql,String key,String value){
		if(sql.contains(key)){
			return sql.replaceFirst(key,value);
		}
		return sql;
	}

	private IdGenExtractor idGenExtractor = new IdGenExtractor();
	
	class IdGenExtractor implements ResultSetExtractor<IdGenInfo> {

        @Override
        public IdGenInfo extractData(ResultSet rs) throws SQLException {
            if (rs.next()) {
                IdGenInfo info = new IdGenInfo();
                info.setId(rs.getLong("id"));
                info.setLastIssued(rs.getLong("last_issued"));
                info.setProjectName(rs.getString("project_name"));
                info.setModelName(rs.getString("model_name"));
                info.setCreateAt(rs.getDate("create_at"));
                info.setUpdateAt(rs.getDate("update_at"));
                return info;
            } else {
                return null;
            }
        }
    }
}
