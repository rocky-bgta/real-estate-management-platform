# Frontend Page and Navigation Structure

```mermaid
flowchart TD
 HOME[Home / Search] --> RESULTS[Search Results]
 RESULTS --> DETAILS[Property Details]
 DETAILS --> BOOK[Booking Request]
 HOME --> LOGIN[Login / Register]
 LOGIN --> DASH[User Dashboard]
 DASH --> MYPROP[My Properties]
 DASH --> MYBOOK[My Bookings]
 DASH --> ADS[Advertisements]
 DASH --> PROFILE[Profile]
 LOGIN --> ADMIN[Admin Dashboard]
 ADMIN --> APPROVAL[Property Approvals]
 ADMIN --> BOOKINGS[Booking Management]
 ADMIN --> USERS[User Management]
 ADMIN --> ADADMIN[Advertisement Management]
```
