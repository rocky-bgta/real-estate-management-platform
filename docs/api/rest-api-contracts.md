# REST API Contracts

Base path: `/api/v1`

| Area | Method | Endpoint | Access |
|---|---|---|---|
| Auth | POST | /auth/register | Public |
| Auth | POST | /auth/login | Public |
| Auth | POST | /auth/refresh | Public with refresh token |
| Properties | GET | /properties | Public |
| Properties | GET | /properties/{id} | Public |
| Properties | POST | /properties | OWNER/ADMIN |
| Properties | PUT | /properties/{id} | Owner/ADMIN |
| Properties | DELETE | /properties/{id} | Owner/ADMIN |
| Properties | POST | /properties/{id}/images | Owner/ADMIN |
| Admin | POST | /admin/properties/{id}/approve | ADMIN |
| Admin | POST | /admin/properties/{id}/reject | ADMIN |
| Bookings | POST | /bookings | CUSTOMER/ADMIN |
| Bookings | GET | /bookings/me | Authenticated |
| Admin | PATCH | /admin/bookings/{id}/status | ADMIN |
| Ads | POST | /advertisements | OWNER |
| Admin | PATCH | /admin/advertisements/{id}/status | ADMIN |

Search query parameters include `city`, `area`, `listingType`, `minPrice`, `maxPrice`, `bedrooms`, `bathrooms`, `minSizeSqFt`, `page`, `size`, and `sort`.
