package com.android.provider.userdata;

import android.net.Uri;

public class Userdata {

	public static final String DATABASE_NAME = "userdata.db";
	public static final int VERSION = 1;

	public static final String AUTHORITY = "com.android.provider.userdata";

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.android.userdata";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.android.userdata";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/item/");
	public static final int QUERYSYSCODE = 0;
	public static final int UPDATESYSCODE = 1;
	public static final int QUERYHMB = 2;
	public static final int DEL_HMB = 3;
	public static final int UPDATEHMB = 4;
	public static final int QUERY_VERSION = 5;
	public static final int DEL_VERSION = 6;
	public static final int ADD_VERSION = 7;

	public class SysCode {
		public static final String TABLE_NAME = "SYS_CODE";
		public static final String CODE_NAME = "CODE_NAME";
		public static final String CODE_VALUE = "CODE_VALUE";
		public static final String MS = "MS";
	}

	public class Hmb {
		public static final String TABLE_NAME = "T_HMB";
		public static final String HDID = "hdid";
		public static final String JSHM = "jshm";
		public static final String DQHM = "dqhm";
		public static final String HDZL = "hdzl";
		public static final String ZQMJ = "zqmj";
	}

	public class Version {
		public static final String TABLE_NAME = "VERSION";
		public static final String ZBBH = "zbbh";
		public static final String GXSM = "gxsm";
		public static final String GXDZ = "gxdz";
	}

}
