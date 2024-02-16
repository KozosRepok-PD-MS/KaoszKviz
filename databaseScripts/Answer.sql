use kaoszkviz

create function findAnswerByQuestionId
(
	@questionId bigint
)
returns table as
	return select * from answer where question_id = @questionId

create function findAnswerByQuestionIdAndOrdinalNumber
(
	@questionId bigint,
    @ordinalNumber smallint
)
returns table as
	return select * from answer where question_id = @questionId AND ordinal_number = @ordinalNumber;

CREATE PROCEDURE deleteAnswerByQuestionIdAndOrdinalNumber
    @questionId bigint,
    @ordinalNumber smallint
AS
BEGIN
    DELETE FROM answer WHERE question_id = @questionId AND ordinal_number = @ordinalNumber;
END
