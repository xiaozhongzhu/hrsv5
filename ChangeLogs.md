# 5.0.1
- 修改生成主键的方法去掉之前的keytable的方式，將使用Snowflake的方式，并将原来的long10位修改为long18位
- 修改获取连接池参数maxPoolSize错误的bug
- 新增对isIpAddr isPort两个方法，判断ip地址、端口的合法性
- 修改所有的抛出异常的方法，修改为BusinessProcessException异常，因为需要将信息抛给页面
- 添加读取appinfo配置文件中projectId参数，同时添加对配置文件中properties参数可以任意定义key-value的方式
- 添加addData方法：支持测试用例直接可以传入一个实体bean
- 添加toJsonForJS方法：支持long类型的长度，如果超出16位默认转存字符串返回给页面
- 获取主键类型改为long类型，之前为String类型
- 使用代码项写死的switch方法，修改为使用if else的方式
- 管控代码中，在action中使用new DatabaseWrapper改为直接使用Dbo
- 发送数据到agent新增user_id
- agent写文件时,缓存的行数使用JobConstant类常量
- agent写文件时，根据系统参数拼接操作日期、操作时间、操作人
- 修改使用Validator中的断言要进行判断数据的合法性
- 增加下载数据字典的接口，方便在没有配置问出后直接可以获取到数据字典