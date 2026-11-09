export type ListingType = "SALE" | "RENT";
export type Category = "HOUSE" | "COMMERCIAL" | "LAND";
export type PropertyStatus = "PENDING_APPROVAL" | "PUBLISHED" | "REJECTED" | "UNAVAILABLE";

export interface Property {
  id: string;
  ownerId: string;
  title: string;
  description?: string;
  category: Category;
  listingType: ListingType;
  price: number;
  city: string;
  area: string;
  sizeSqFt?: number;
  bedrooms?: number;
  bathrooms?: number;
  status: PropertyStatus;
}

export interface AuthSession {
  accessToken: string;
  userId: string;
  email: string;
  role: "USER" | "OWNER" | "ADMIN";
}

const API = import.meta.env.VITE_API_URL ?? "http://localhost:8080/api/v1";

export async function request<T>(path: string, init: RequestInit = {}): Promise<T> {
  const session = sessionStorage.getItem("estate-session");
  const token = session ? (JSON.parse(session) as AuthSession).accessToken : undefined;
  const response = await fetch(API + path, {
    ...init,
    headers: {
      ...(init.body instanceof FormData ? {} : { "Content-Type": "application/json" }),
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...init.headers,
    },
  });
  if (!response.ok) {
    const body = await response.json().catch(() => ({ message: "Request failed" }));
    throw new Error(body.message ?? "Request failed");
  }
  if (response.status === 204) return undefined as T;
  return response.json() as Promise<T>;
}
