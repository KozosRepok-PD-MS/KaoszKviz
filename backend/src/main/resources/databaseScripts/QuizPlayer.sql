
create or alter function findQuizPlayerByQuizHistoryId
(
    @quizHistoryId bigint
)
returns table as
    return select * from quiz_player where quiz_history_id = @quizHistoryId