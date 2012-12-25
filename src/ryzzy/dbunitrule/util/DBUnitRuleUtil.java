package ryzzy.dbunitrule.util;

import java.sql.Connection;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;

import ryzzy.dbunitrule.exception.DBUnitRuleException;

public class DBUnitRuleUtil {
	private static DataSetLoader getDataSetLoader(String ext) {
		return DataSetLoader.valueOf(ext);
	}

	public static IDataSet loadDataSet(String filePath) {
		String ext = StringUtil.getExtention(filePath).toUpperCase();
		return getDataSetLoader(ext).load(filePath);
	}

	public static void storeDataSet(IDataSet dataset, String filePath) {
		String ext = StringUtil.getExtention(filePath).toUpperCase();
		getDataSetLoader(ext).store(dataset, filePath);
	}

	public static IDataSet queryDataSet(Connection con, String... tableNames) {
		try {
			QueryDataSet queryDataSet = new QueryDataSet(new DatabaseConnection(con), false);
			for (String tableName : tableNames) {
				queryDataSet.addTable(tableName);
			}
			return queryDataSet;
		} catch (Exception e) {
			throw new DBUnitRuleException(e);
		}
	}

	public static void cleanInsert(Connection con, String insertFilePath) {
		try {
			DatabaseOperation.CLEAN_INSERT.execute(new DatabaseConnection(con), loadDataSet(insertFilePath));
		} catch (Exception e) {
			throw new DBUnitRuleException(e);
		}
	}

	public static void insert(Connection con, String insertFilePath) {
		try {
			DatabaseOperation.INSERT.execute(new DatabaseConnection(con), loadDataSet(insertFilePath));
		} catch (Exception e) {
			throw new DBUnitRuleException(e);
		}
	}

	public static IDataSet backupCleanInsert(Connection con, String insertFilePath, String backupFilePath)
	{
		return backupDoDBOperation(con, insertFilePath, backupFilePath, new DBOperatable() {
			@Override
			public void run(Connection con, IDataSet dataset) throws DatabaseUnitException, SQLException {
				DatabaseOperation.CLEAN_INSERT.execute(new DatabaseConnection(con), dataset);
			}
		});
	}

	public static IDataSet backupInsert(Connection con, String insertFilePath, String backupFilePath) {
		return backupDoDBOperation(con, insertFilePath, backupFilePath, new DBOperatable() {
			@Override
			public void run(Connection con, IDataSet dataset) throws DatabaseUnitException, SQLException {
				DatabaseOperation.INSERT.execute(new DatabaseConnection(con), dataset);
			}
		});
	}

	public static IDataSet backupDelete(final Connection con, final String insertFilePath, final String backupFilePath) {
		return backupDoDBOperation(con, insertFilePath, backupFilePath, new DBOperatable() {
			@Override
			public void run(Connection con, IDataSet dataset) throws DatabaseUnitException, SQLException {
				DatabaseOperation.DELETE.execute(new DatabaseConnection(con), dataset);
			}
		});
	}

	private static IDataSet backupDoDBOperation(Connection con, String insertFilePath, String backupFilePath, DBOperatable run) {
		try
		{
			IDataSet insertIDataSet = loadDataSet(insertFilePath);
			IDataSet backupIDataSet = queryDataSet(con, insertIDataSet.getTableNames());
			storeDataSet(backupIDataSet, backupFilePath);
			run.run(con, insertIDataSet);
			return backupIDataSet;
		} catch (Exception e)
		{
			throw new DBUnitRuleException(e);
		}
	}

	private static interface DBOperatable {
		void run(Connection con, IDataSet dataset) throws DatabaseUnitException, SQLException;
	}
}
