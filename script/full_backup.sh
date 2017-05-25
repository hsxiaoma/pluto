#!/bin/sh
print_and_clear()
{
cat E:/project/CORE_TA/pluto/temp/1495175848400.tmp
rm -rf E:/project/CORE_TA/pluto/temp/1495175848400.tmp
}

check_result()
{
if [ -z "`tail -1 E:/project/CORE_TA/pluto/temp/1495175848400.tmp| grep 'completed OK!'`" ] ; then
print_and_clear
exit 1
fi
}

innobackupex --defaults-file=/etc/my-mutual.cnf --user=root --password=root --use-memory=4G --parallel=8 /backup > E:/project/CORE_TA/pluto/temp/1495175848400.tmp 2>&1 
check_result
print_and_clear
