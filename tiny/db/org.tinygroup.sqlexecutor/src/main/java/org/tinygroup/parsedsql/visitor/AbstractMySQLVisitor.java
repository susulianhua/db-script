/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.parsedsql.visitor;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.parsedsql.SQLBuilder;
import org.tinygroup.parsedsql.SqlParsedResult;
import org.tinygroup.parsedsql.base.DatabaseType;
import org.tinygroup.parsedsql.parser.SQLVisitor;
import org.tinygroup.parsedsql.util.SQLUtil;

/**
 *
 * @author ballackhui
 *
 */
public abstract class AbstractMySQLVisitor extends MySqlOutputVisitor implements
        SQLVisitor {

    private Context context;
    
    private SqlParsedResult parsedResult;

    public AbstractMySQLVisitor() {
        super(new SQLBuilder());
        setPrettyFormat(false);
        this.parsedResult=new SqlParsedResult();
    }


    @Override
    public final DatabaseType getDatabaseType() {
        return DatabaseType.MySQL;
    }


    @Override
    public final SQLBuilder getSQLBuilder() {
        return (SQLBuilder) appender;
    }

    @Override
    public final void printToken(final String token) {
        getSQLBuilder().appendToken(SQLUtil.getExactlyValue(token));
    }

    @Override
    public final void printToken(final String label, final String token) {
        getSQLBuilder().appendToken(label, SQLUtil.getExactlyValue(token));
    }
    
    @Override
	public SqlParsedResult getParsedResult() {
		return parsedResult;
	}

	/**
     * 父类使用<tt>@@</tt>代替<tt>?</tt>,此处直接输出参数占位符<tt>?</tt>
     *
     * @param x
     *            变量表达式
     * @return false 终止遍历AST
     */
    @Override
    public final boolean visit(final SQLVariantRefExpr x) {
        print(x.getName());
        return false;
    }

    @Override
    public boolean visit(final SQLBinaryOpExpr x) {
        SQLExpr right = x.getRight();
        SQLExpr left = x.getLeft();
        SQLVariantRefExpr refExpr = null;
        if (right instanceof SQLVariantRefExpr) {
            refExpr = (SQLVariantRefExpr) right;
        } else if (left instanceof SQLVariantRefExpr) {
            refExpr = (SQLVariantRefExpr) left;
        }
        if (refExpr != null) {
            String refName = refExpr.getName();
            Object paramValue = getContext().get(StringUtil.substringAfter(refName, "@"));
            if (paramValue == null) {
                print(" 1=1 ");
                return false;
            }
        }
		/*if(right instanceof SQLCharExpr){
			SQLCharExpr charExpr=(SQLCharExpr)right;
			String text = charExpr.getText();
			int startIndex=0;
			int endIndex=text.length();
			if(text.startsWith("_")||text.startsWith("%")){
				startIndex=1;
			}
			if(text.endsWith("_")||text.endsWith("%")){
				endIndex=text.length()-1;
			}
			String replaceText=text.substring(startIndex, endIndex);
			Object paramValue = getContext().get(StringUtil.substringAfter(replaceText, "@"));
			if(paramValue==null){
			     print(" 1=1 ");	
			     return false;
			}
		}*/
        return super.visit(x);
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }
}
