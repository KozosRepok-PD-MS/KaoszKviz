
CREATE or ALTER FUNCTION findUserByName(
    @name nvarchar(20)
)
RETURNS table AS
    RETURN SELECT * FROM usr WHERE username LIKE '%' + @name + '%'

GO

CREATE OR ALTER FUNCTION findUserByLoginBase(
    @loginBase nvarchar(50)
)
RETURNS table AS
    RETURN SELECT * FROM usr WHERE username = @loginBase OR email = @loginBase

GO

CREATE OR ALTER FUNCTION getUserByAPIKey(
    @apiKey uniqueidentifier
)
RETURNS TABLE AS
    RETURN SELECT TOP 1 * FROM usr WHERE id = (SELECT user_id FROM api_key WHERE client_secret = @apiKey)
