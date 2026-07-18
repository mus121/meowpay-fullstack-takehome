"use client";

import { useQuery } from "@tanstack/react-query";
import { fetchCats } from "@/lib/api/cats";
import { QUERY_KEYS } from "@/constants";

export function useCats() {
  return useQuery({
    queryKey: QUERY_KEYS.cats,
    queryFn: fetchCats,
  });
}
