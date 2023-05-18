export class Answer{
    id: number;
    text: string;
    username: string;

    constructor(a: Answer){
        this.id = a.id;
        this.text = a.text;
        this.username = a.username;
    }
}

export class Question {
    id : number;
    text: String;
    userId: number;
    username: String;
    answers: Answer[];
    sumVotes: number;

    constructor(q: Question){
        this.id=q.id;
        this.text = q.text;
        this.userId = q.userId;
        this.username = q.username;

        this.answers = q.answers;
        this.sumVotes = q.sumVotes;
    }
}
