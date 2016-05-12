#[查看最近爬取下来的网页的时间]
select url,time from data order by id desc limit 1 

#[查看每个种子源的抓取情况]
SELECT seed,count(seed),max(level) from data  GROUP BY seed 

#[查看抓取了600张网页以上的或者抓取深度超过10的种子站点]
SELECT seed,count(seed),max(level) from data  GROUP BY seed having max(level) >= 10 OR count(seed) >=600 

#[将抓取了600张网页以上的或者抓取深度为10的种子站点置为1]
update seeds set enable = 1 where url in (
	SELECT seed from data  GROUP BY seed having max(level) >= 10 OR count(seed) >=600
) 

#[将尚未完成的种子站点抓取下来的数据置空，重新开始]
DELETE from `data` where seed in (select url from seeds where ENABLE=0)