/**
 * Copyright 2007 The Apache Software Foundation
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hbase.shell;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseAdmin;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HConnection;
import org.apache.hadoop.hbase.HConnectionManager;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.io.Text;

public class CreateCommand extends BasicCommand {
  
  private Text table;
  private List<String> columnfamilies;
  @SuppressWarnings("unused")
  private int limit;

  public ReturnMsg execute(Configuration conf) {
    if (this.table == null || this.columnfamilies == null)
      return new ReturnMsg(0, "Syntax error : Please check 'Create' syntax.");

    try {
      HConnection conn = HConnectionManager.getConnection(conf);
      HBaseAdmin admin = new HBaseAdmin(conf);
      
      if (conn.tableExists(this.table)) {
        return new ReturnMsg(0, "Table was already exsits.");
      }
      HTableDescriptor desc = new HTableDescriptor(this.table.toString());
      for (int i = 0; i < this.columnfamilies.size(); i++) {
        String columnFamily = columnfamilies.get(i);
        if (columnFamily.lastIndexOf(':') == (columnFamily.length() - 1)) {
          columnFamily = columnFamily.substring(0, columnFamily.length() - 1);
        }
        desc.addFamily(new HColumnDescriptor(columnFamily + FAMILY_INDICATOR));
      }
      admin.createTable(desc);
      return new ReturnMsg(1, "Table created successfully.");
    } catch (IOException e) {
      return new ReturnMsg(0, "error msg : " + e.toString());
    }
  }

  public void setTable(String table) {
    this.table = new Text(table);
  }

  public void setColumnfamilies(List<String> columnfamilies) {
    this.columnfamilies = columnfamilies;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }
  
}
