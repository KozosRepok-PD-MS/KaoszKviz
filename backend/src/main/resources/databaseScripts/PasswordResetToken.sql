CREATE OR ALTER FUNCTION getPasswordResetTokenByKey(
    @resetKey uniqueidentifier
)
RETURNS TABLE AS
    RETURN SELECT * FROM password_reset_token WHERE reset_key = @resetKey

GO
