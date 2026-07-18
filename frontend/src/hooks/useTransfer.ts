"use client";

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createTransfer, fetchTransfers } from "@/lib/api/transfers";
import { QUERY_KEYS } from "@/constants";
import type { CreateTransferInput } from "@/types";

export function useTransfers() {
  return useQuery({
    queryKey: QUERY_KEYS.transfers,
    queryFn: fetchTransfers,
  });
}

export function useCreateTransfer() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (input: CreateTransferInput) => createTransfer(input),
    onSuccess: async () => {
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: QUERY_KEYS.cats }),
        queryClient.invalidateQueries({ queryKey: QUERY_KEYS.transfers }),
      ]);
    },
  });
}
