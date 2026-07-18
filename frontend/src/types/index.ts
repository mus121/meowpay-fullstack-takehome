export type Cat = {
  id: string;
  name: string;
  balance: number;
};

export type CatSummary = {
  id: string;
  name: string;
};

export type Transfer = {
  id: string;
  sender: CatSummary;
  recipient: CatSummary;
  amount: number;
  createdAt: string;
};

export type CreateTransferInput = {
  senderId: string;
  recipientId: string;
  amount: number;
};
