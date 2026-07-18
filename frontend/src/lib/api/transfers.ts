import { apiRequest } from "@/lib/api/client";
import type { CreateTransferInput, Transfer } from "@/types";

export function fetchTransfers(): Promise<Transfer[]> {
  return apiRequest<Transfer[]>("/api/transfers");
}

export function createTransfer(input: CreateTransferInput): Promise<Transfer> {
  return apiRequest<Transfer>("/api/transfers", {
    method: "POST",
    body: JSON.stringify(input),
  });
}
