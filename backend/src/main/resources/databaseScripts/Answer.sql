use kaoszkviz

GO

create or alter function findAnswerByQuestionId
(
	@questionId bigint
)
returns table as
	return select * from answer where question_id = @questionId

GO

create or alter function findAnswerByQuestionIdAndOrdinalNumber
(
	@questionId bigint,
    @ordinalNumber smallint
)
returns table as
	return select * from answer where question_id = @questionId AND ordinal_number = @ordinalNumber;

GO

create or alter PROCEDURE deleteAnswerByQuestionIdAndOrdinalNumber
    @questionId bigint,
    @ordinalNumber smallint
AS
BEGIN
    DELETE FROM answer WHERE question_id = @questionId AND ordinal_number = @ordinalNumber;
END
