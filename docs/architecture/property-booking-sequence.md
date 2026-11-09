# Property Listing and Booking Sequence

```mermaid
sequenceDiagram
 participant O as Owner
 participant API as Backend
 participant A as Admin
 participant C as Customer
 O->>API: Submit property
 API-->>O: PENDING_APPROVAL
 A->>API: Approve property
 API-->>A: PUBLISHED
 C->>API: Search/view property
 C->>API: Create booking
 API-->>C: PENDING
 A->>API: Confirm booking
 API-->>C: CONFIRMED + payment requirement
 Note over API,C: Live payment processing remains TODO
```
