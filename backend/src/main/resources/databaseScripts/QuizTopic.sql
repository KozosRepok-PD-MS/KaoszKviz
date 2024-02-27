
create or alter function findQuizTopicByQuizId
(
    @quizId bigint
)
returns table
as 
    return select * from quiz_topic where quiz_id = @quizId