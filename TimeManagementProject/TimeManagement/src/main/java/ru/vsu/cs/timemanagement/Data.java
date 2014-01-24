package ru.vsu.cs.timemanagement;


import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Наталья on 12.01.14.
 */
@DatabaseTable(tableName = "tasks")
public class Data {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public String name;

    @DatabaseField
    public String description;

    @DatabaseField
    public boolean important;

    @DatabaseField
    public boolean urgent;

    /*@DatabaseField
    public boolean done;*/

    public Data(String _name, String _descr, boolean _imp, boolean _urg) {
        name = _name;
        description = _descr;
        important = _imp;
        urgent = _urg;
    }

    public Data(String _name, boolean _imp, boolean _urg) {
        name = _name;
        important = _imp;
        urgent = _urg;
    }

    public Data(){}

    public void save(Context context) {

        DatabaseHelper helper = new DatabaseHelper(context);
        try {

            Dao<Data, Integer> dao = helper.getDataDao();
            dao.create(this);

        } catch (SQLException e) {

            throw new RuntimeException(e);

        } finally {

            helper.close();
        }
    }

    public static List<Data> returnData(boolean _import, boolean _urg, Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        try {

            Dao<Data, Integer> dao = helper.getDataDao();
            QueryBuilder<Data, Integer> qb = dao.queryBuilder();
           // qb.where().eq("important", _import);//.and().eq("urgent", _urg);
            qb.where().eq("urgent", _urg).and().eq("important", _import);
            PreparedQuery<Data> preparedQuery = qb.prepare();
            List<Data> tasks = dao.query(preparedQuery);
            return tasks;
            //return dao.queryForAll();


        } catch (SQLException e) {

            throw new RuntimeException(e);

        } finally {

            helper.close();

        }
    }

    public static void updateField(String compare, String _name, String _descr, boolean _import, boolean _urg, Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        try {
            Dao<Data, Integer> dao = helper.getDataDao();
            UpdateBuilder<Data, Integer> updateBuilder = dao.updateBuilder();
            updateBuilder.updateColumnValue("name", _name);
            updateBuilder.updateColumnValue("description", _descr);
            updateBuilder.updateColumnValue("important", _import);
            updateBuilder.updateColumnValue("urgent", _urg);
            updateBuilder.where().eq("name", compare);
            updateBuilder.update();

        } catch (SQLException e) {

            throw new RuntimeException(e);

        } finally {

            helper.close();

        }

    }

    public static List<Data> returnDone(boolean _done, Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        try {
            Dao<Data, Integer> dao = helper.getDataDao();
            QueryBuilder<Data, Integer> qb = dao.queryBuilder();
            qb.where().eq("done", _done);
            PreparedQuery<Data> preparedQuery = qb.prepare();
            List<Data> tasks = dao.query(preparedQuery);
            return tasks;

        } catch (SQLException e) {

            throw new RuntimeException(e);

        } finally {

            helper.close();

        }

    }
}


