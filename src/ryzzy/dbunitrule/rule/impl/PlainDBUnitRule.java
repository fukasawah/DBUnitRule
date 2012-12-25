package ryzzy.dbunitrule.rule.impl;

import java.sql.Connection;

import ryzzy.dbunitrule.rule.AbstractDBUnitRule;

public class PlainDBUnitRule extends AbstractDBUnitRule {

	public PlainDBUnitRule(Connection con) {
		super(con);
	}

}
