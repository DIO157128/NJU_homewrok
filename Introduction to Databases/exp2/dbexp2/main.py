def exe4_1():
    import pymysql
    db = pymysql.connect(host="localhost", user="root",
                         password="ybw064289", database="orderdb",
                         charset="utf8")
    cur = db.cursor()
    sql = '''
    select employeeNo,employeeName,salary
    from employee
    order by salary desc ;
    '''
    cur.execute(sql)
    for i in cur.fetchmany(20):
        print(i)
    db.commit()
    print("OK")
    cur.close()
    db.close()
def exe4_2():
    import pymysql
    db = pymysql.connect(host="localhost", user="root",
                         password="ybw064289", database="orderdb",
                         charset="utf8")
    cur = db.cursor()
    sql = '''
    insert into customer
    values('C20080002','泰康股份有限公司','010-5422685','天津市','220501')
    '''
    cur.execute(sql)
    db.commit()
    print("OK")
    cur.close()
    db.close()
def exe4_3():
    import pymysql
    db = pymysql.connect(host="localhost", user="root",
                         password="ybw064289", database="orderdb",
                         charset="utf8")
    cur = db.cursor()
    sql = '''
    delete from employee
    where salary>5000
    '''
    cur.execute(sql)
    db.commit()
    print("OK")
    cur.close()
    db.close()
def exe4_4():
    import pymysql
    db = pymysql.connect(host="localhost", user="root",
                         password="ybw064289", database="orderdb",
                         charset="utf8")
    cur = db.cursor()
    sql = '''
    update product
    set productPrice=1.5*productPrice
    where productPrice>1000
    '''
    cur.execute(sql)
    db.commit()
    print("OK")
    cur.close()
    db.close()
def exe5_1():
    import pymysql
    db = pymysql.connect(host="localhost", user="root",
                         password="ybw064289", database="orderdb",
                         charset="utf8")
    cur = db.cursor()
    sql = '''
        update employee
        set salary=salary+200
        where department='业务科';
    '''
    cur.execute(sql)
    db.commit()
    print("OK")
    cur.close()
    db.close()
def exe5_2():
    import pymysql
    db = pymysql.connect(host="localhost", user="root",
                         password="ybw064289", database="orderdb",
                         charset="utf8")
    cur = db.cursor()
    sql = '''
        select customerName,address,telephone
        from customer
    '''
    cur.execute(sql)
    for i in cur.fetchall():
        print(i)
    db.commit()
    print("OK")
    cur.close()
    db.close()
# exe4_1()
# exe4_2()
# exe4_3()
# exe4_4()
# exe5_1()
exe5_2()




