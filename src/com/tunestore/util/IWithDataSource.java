package com.tunestore.util;

import javax.sql.DataSource;

public interface IWithDataSource {
  public abstract void setDataSource(DataSource dataSource);
}
