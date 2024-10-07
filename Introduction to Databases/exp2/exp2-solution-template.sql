-- 姓名：XXX
-- 学号：XXX
-- 提交前请确保本次实验独立完成，若有参考请注明并致谢。

-- ____________________________________________________________________________________________________________________________________________________________________________________________________________
-- BEGIN Q1.1
drop procedure if exists `sproduct1`;
create procedure sproduct1(in Name varchar(20))
		select ordermaster.customerNo,customer.customerName,ordermaster.orderNo,orderdetail.quantity,orderdetail.price
        from ordermaster,customer,orderdetail,product
        where ordermaster.orderNo=orderdetail.orderNo 
        and orderdetail.productNo=product.productNo 
        and customer.customerNo=ordermaster.customerNo
        and product.productName=Name
        order by orderdetail.price desc;
call sproduct1('32M DRAM');
-- END Q1.1

-- ____________________________________________________________________________________________________________________________________________________________________________________________________________
-- BEGIN Q1.2
drop procedure if exists `semployee3`;
create procedure semployee3(in No char(8))
	select employeeNo,employeeName,gender,hireDate,department
    from employee
    where department =(select department from employee where employeeNo=No) and hireDate<(select hireDate from employee where employeeNo=No);
call semployee3('E2008005');

-- END Q1.2

-- ____________________________________________________________________________________________________________________________________________________________________________________________________________
-- BEGIN Q2.1
drop function if exists `saverage`;
set global log_bin_trust_function_creators=1;
delimiter $$
create function saverage (name varchar(20))
returns integer
begin
declare res integer;
select avg(orderdetail.price) into res
from product,orderdetail
where productName=name
and product.productNo=orderdetail.productNo;
return res;
end$$
delimiter ;
select distinct product.productName,saverage(product.productName)
from product,orderdetail;
-- END Q2.1

-- ____________________________________________________________________________________________________________________________________________________________________________________________________________
-- BEGIN Q2.2
drop function if exists `ssum`;
set global log_bin_trust_function_creators=1;
delimiter $$
create function ssum (no char(9))
returns integer
begin
declare res integer;
select sum(orderdetail.quantity) into res
from orderdetail
where productNo=no;
return res;
end$$
delimiter ;
select product.productNo,product.productName,ssum(product.productNo)
from product 
where ssum(product.productNo)>4;
-- END Q2.2

-- ____________________________________________________________________________________________________________________________________________________________________________________________________________
-- BEGIN Q3.1
delimiter $$
create trigger trigger1
after insert 
on product for each row
begin
if (new.productPrice>1000)
then set new.productPrice=1000;
end if;
end$$
delimiter ;
insert into product values('P20080004', '龙基777F8纯平显示器', '显示器', '1800.00'
)
-- END Q3.1

-- ____________________________________________________________________________________________________________________________________________________________________________________________________________
-- BEGIN Q3.2
delimiter $$
create trigger trigger2
before insert 
on ordermaster for each row
begin
if (select employee.hireDate from employee where new.employeeNo=employee.employeeNo<'1992-01-01 00:00:00')
then set employee.salary=employee.salary*1.08;
else set employee.salary=employee.salary*1.05;
end if;
end$$
delimiter ;
insert into ordermaster values('200806120002', 'C20050002', 'E2005002', '2008-06-12 00:00:00', '0.00', 'I000000010'
)
-- END Q3.2

