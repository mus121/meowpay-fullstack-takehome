import { z } from "zod";

export const transferSchema = z
  .object({
    senderId: z.string().uuid("Select a sender"),
    recipientId: z.string().uuid("Select a recipient"),
    amount: z.coerce
      .number({ message: "Enter a valid amount" })
      .positive("Amount must be greater than zero")
      .max(1_000_000, "Amount is too large")
      .refine(
        (value) => Number.isFinite(value) && Math.round(value * 100) === value * 100,
        "Use at most 2 decimal places",
      ),
  })
  .refine((data) => data.senderId !== data.recipientId, {
    message: "Sender and recipient must be different cats",
    path: ["recipientId"],
  });

export type TransferFormValues = z.infer<typeof transferSchema>;
