package ryzzy.dbunitrule.rule.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.rules.TemporaryFolder;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import ryzzy.dbunitrule.exception.DBUnitRuleException;
import ryzzy.dbunitrule.rule.AbstractDBUnitRule;
import ryzzy.dbunitrule.util.DBUnitRuleUtil;
import ryzzy.dbunitrule.util.StringUtil;

public class FileBackupDBUnitRule extends AbstractDBUnitRule {
	public FileBackupDBUnitRule(Connection con) {
		super(con);
	}

	private final TemporaryFolder temporaryFolder = new TemporaryFolder();
	private final List<String> backupFileList = new ArrayList<String>();

	@Override
	public void cleanInsert(String filePath) {
		temporaryFolder.newFolder("dbunitrule");
		String backupFilePath;
		try {
			String filename = StringUtil.getFilename(filePath);
			backupFilePath = temporaryFolder.newFile(filename + ".backup.xls").getAbsolutePath();
		} catch (IOException e) {
			throw new DBUnitRuleException(e);
		}
		DBUnitRuleUtil.backupCleanInsert(getConnection(), filePath, backupFilePath);
		backupFileList.add(backupFilePath);
	}

	@Override
	protected void rollback() throws SQLException {
		List<String> list = new ArrayList<String>(backupFileList);
		Collections.reverse(list);

		for (String file : list) {
			try {
				DBUnitRuleUtil.cleanInsert(getConnection(), file);
			} catch (DBUnitRuleException e) {
				// TODO ummm...
				e.printStackTrace();
			}
		}
		super.rollback();
	}

	@Override
	public Statement apply(Statement base, Description description) {
		return temporaryFolder.apply(super.apply(base, description), description);
	}
}
