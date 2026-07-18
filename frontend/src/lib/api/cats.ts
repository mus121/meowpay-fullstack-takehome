import { apiRequest } from "@/lib/api/client";
import type { Cat } from "@/types";

export function fetchCats(): Promise<Cat[]> {
  return apiRequest<Cat[]>("/api/cats");
}

export function fetchCat(id: string): Promise<Cat> {
  return apiRequest<Cat>(`/api/cats/${id}`);
}
