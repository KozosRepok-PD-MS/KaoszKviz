
export interface Quiz {
    id?: number;
    isPublic?: boolean;
    ownerId?: number;
    title?: string;
    description?: string;
    shortAccessibleName?: string;
    mediaOwnerId?: string;
    mediaFileName?: string;
}

export type TQuizList = Quiz[];

export interface QuizCreateFormType {

}

export interface QuizModifyFormType {

}
