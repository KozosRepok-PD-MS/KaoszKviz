
create or alter function findTopicByString
(
    @searchString nvarchar(20)
)
returns table
as 
    return select * from topic where title like '%' + @searchString + '%'
