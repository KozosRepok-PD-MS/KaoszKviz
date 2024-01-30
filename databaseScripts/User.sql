use kaoszkviz

GO 

CREATE FUNCTION findUserByName(
	@name nvarchar(20)
)
RETURNS table AS
	RETURN SELECT * FROM usr WHERE username LIKE '%' + @name + '%'

GO