export interface Answer {
    questionId?: bigint;
    ordinalNumber?: number;
    answer?: string;
    correct?: boolean;
    correctAnswer?: string;
}

export type TAnswerList = Answer[];

export interface AnswerCreateFormType {
    questionId?: String; //!TODO nem string
    answer?: string;
    correct?: boolean;
    correctAnswer?: string;
}

export interface AnswerModifyFormType {

}