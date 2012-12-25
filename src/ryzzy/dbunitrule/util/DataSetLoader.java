package ryzzy.dbunitrule.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.excel.XlsDataSetWriter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.dataset.xml.FlatXmlWriter;

import ryzzy.dbunitrule.exception.DBUnitRuleException;

public enum DataSetLoader {
	XML {

		@Override
		IDataSet load(InputStream is) throws Exception {
			return new FlatXmlDataSetBuilder().build(is);
		}

		@Override
		void store(IDataSet dataset, OutputStream os) throws Exception {
			new FlatXmlWriter(os).write(dataset);
		}
	},
	XLS {

		@Override
		IDataSet load(InputStream is) throws Exception {
			return new XlsDataSet(is);
		}

		@Override
		void store(IDataSet dataset, OutputStream os) throws Exception {
			new XlsDataSetWriter().write(dataset, os);
		}
	},
	XLSX { // TODO no check.
		@Override
		IDataSet load(InputStream is) throws Exception {
			return DataSetLoader.XLS.load(is);
		}

		@Override
		void store(IDataSet dataset, OutputStream os) throws Exception {
			DataSetLoader.XLS.store(dataset, os);
		}
	},

	;
	abstract IDataSet load(InputStream is) throws Exception;

	abstract void store(IDataSet dataset, OutputStream os) throws Exception;

	public IDataSet load(String filePath) {
		return load(new File(filePath));
	}

	public IDataSet load(File file) {
		try {
			return load(new FileInputStream(file));
		} catch (Exception e) {
			throw new DBUnitRuleException(e);
		}
	}

	public void store(IDataSet dataset, String filePath) {
		store(dataset, new File(filePath));
	}

	public void store(IDataSet dataset, File file) {
		try {
			store(dataset, new FileOutputStream(file));
		} catch (Exception e) {
			throw new DBUnitRuleException(e);
		}
	}
}
