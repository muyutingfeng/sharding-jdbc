/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingjdbc.core.parsing.parser.dialect.sqlserver.sql;

import io.shardingjdbc.core.parsing.lexer.LexerEngine;
import io.shardingjdbc.core.parsing.parser.dialect.sqlserver.clause.SQLServerOffsetClauseParser;
import io.shardingjdbc.core.parsing.parser.dialect.sqlserver.clause.SQLServerTopClauseParser;
import io.shardingjdbc.core.parsing.parser.dialect.sqlserver.clause.facade.SQLServerSelectClauseParserFacade;
import io.shardingjdbc.core.parsing.parser.sql.dql.select.AbstractSelectParser;
import io.shardingjdbc.core.parsing.parser.sql.dql.select.SelectStatement;
import io.shardingjdbc.core.rule.ShardingRule;

/**
 * Select parser for SQLServer.
 *
 * @author zhangliang
 */
public final class SQLServerSelectParser extends AbstractSelectParser {
    
    private final SQLServerTopClauseParser topClauseParser;
    
    private final SQLServerOffsetClauseParser offsetClauseParser;
    
    public SQLServerSelectParser(final ShardingRule shardingRule, final LexerEngine lexerEngine) {
        super(shardingRule, lexerEngine, new SQLServerSelectClauseParserFacade(shardingRule, lexerEngine));
        topClauseParser = new SQLServerTopClauseParser(lexerEngine);
        offsetClauseParser = new SQLServerOffsetClauseParser(lexerEngine);
    }
    
    @Override
    protected void parseInternal(final SelectStatement selectStatement) {
        parseDistinct();
        parseTop(selectStatement);
        parseSelectList(selectStatement, getItems());
        parseFrom(selectStatement);
        parseWhere(getShardingRule(), selectStatement, getItems());
        parseGroupBy(selectStatement);
        parseHaving();
        parseOrderBy(selectStatement);
        parseOffset(selectStatement);
        parseSelectRest();
    }
    
    private void parseTop(final SelectStatement selectStatement) {
        topClauseParser.parse(selectStatement);
    }
    
    private void parseOffset(final SelectStatement selectStatement) {
        offsetClauseParser.parse(selectStatement);
    }
}
