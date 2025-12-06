# Affiliate Link System Implementation

## Overview
This document describes the complete affiliate link system implementation for the Distribio application. The system allows registered users to share affiliate links that track sales and attribute commissions.

## Backend Implementation

### Database Changes

1. **Users Table**
   - Added `affiliate_code VARCHAR(50) UNIQUE NOT NULL` column
   - Each user gets a unique 10-character affiliate code on registration

2. **Sales Table**
   - Added `affiliate_user_id BIGINT` column (nullable foreign key to users.id)
   - Tracks which affiliate should receive commission for each sale

### API Endpoints

#### 1. GET `/api/aff/{code}`
- **Purpose**: Validate an affiliate code
- **Access**: Public (no authentication required)
- **Response**: 
  ```json
  {
    "affiliateUserId": 123,
    "code": "ABC123XYZ",
    "username": "affiliate_user"
  }
  ```
- **Status Codes**:
  - `200 OK`: Valid affiliate code
  - `404 Not Found`: Invalid affiliate code

#### 2. POST `/api/sales`
- **Purpose**: Create a sale with optional affiliate attribution
- **Request Body**:
  ```json
  {
    "sellerId": 1,
    "buyerId": 2,
    "affiliateUserId": 123,  // Optional - from cookie/localStorage
    "totalAmount": 100.00
  }
  ```
- **Response**: Sale object with affiliate information

#### 3. POST `/api/auth/register`
- **Purpose**: Register new user (updated to handle affiliate codes)
- **Request Body**:
  ```json
  {
    "username": "newuser",
    "email": "user@example.com",
    "password": "password123",
    "referralCode": "REF123",      // Optional
    "affiliateCode": "AFF123"      // Optional - from cookie
  }
  ```
- **Behavior**: 
  - If `affiliateCode` is provided and no `referralCode`, the affiliate becomes the user's parent
  - This creates a referral relationship when visitor registers after clicking affiliate link

### Commission Logic

- **Single-Level Affiliate Commission**: 10% of sale amount
- **Commission is paid to**: The user whose affiliate code was used
- **Conditions**:
  - Affiliate user must be active
  - Affiliate code must be valid at time of sale
  - Commission is recorded in `commission_ledger` table with level 0

### Key Services

1. **AffiliateService**
   - `validateAffiliateCode(String code)`: Validates and returns affiliate info
   - `processAffiliateCommission(Sale sale)`: Processes single-level commission

2. **SaleService** (Updated)
   - `createSale()`: Now accepts optional `affiliateUserId` parameter
   - Processes affiliate commission before referral commissions

3. **AuthService** (Updated)
   - `register()`: Now accepts optional `affiliateCode` parameter
   - Sets affiliate as parent if no referral code provided

## Frontend Implementation

### Utility Functions (`affiliate-utils.js`)

1. **`setAffiliate(affiliateInfo)`**
   - Stores affiliate info in both cookie and localStorage
   - Cookie TTL: 30 days
   - Data structure: `{ affiliateUserId, affiliateCode, timestamp }`

2. **`getAffiliate()`**
   - Retrieves affiliate info from cookie (preferred) or localStorage
   - Validates expiry (30 days)
   - Returns `null` if expired or invalid

3. **`clearAffiliate()`**
   - Removes affiliate info from both cookie and localStorage

4. **`hasValidAffiliate()`**
   - Checks if valid, non-expired affiliate info exists

### React Components

#### 1. AffiliatePage (`/aff/:code`)
- Validates affiliate code via backend API
- Stores affiliate info in cookie/localStorage
- Redirects to `/products` page
- Handles errors gracefully

#### 2. Checkout Component (Updated)
- Reads affiliate info from cookie/localStorage
- Includes `affiliateUserId` in sale request
- Example:
  ```typescript
  const affiliateInfo = getAffiliate();
  const saleRequest = {
    sellerId: sellerId,
    buyerId: buyerId,
    affiliateUserId: affiliateInfo?.affiliateUserId || null,
    totalAmount: totalAmount
  };
  ```

#### 3. Register Component (Updated)
- Reads affiliate code from cookie/localStorage
- Includes `affiliateCode` in registration request
- Clears affiliate cookie after successful registration
- Example:
  ```typescript
  const affiliateInfo = getAffiliate();
  const registerRequest = {
    username: username,
    email: email,
    password: password,
    affiliateCode: affiliateInfo?.affiliateCode || null
  };
  ```

## Database Migration

Run the migration script: `src/main/resources/migration_add_affiliate.sql`

This script:
- Adds `affiliate_code` column to users table
- Adds `affiliate_user_id` column to sales table
- Creates indexes for performance
- Adds foreign key constraint

## Flow Diagram

### Affiliate Link Click Flow
1. User clicks affiliate link: `https://myapp.com/aff/ABC123XYZ`
2. Frontend validates code via `GET /api/aff/ABC123XYZ`
3. If valid, store `{ affiliateUserId, affiliateCode, timestamp }` in cookie/localStorage
4. Redirect to `/products` page
5. User browses and purchases
6. On checkout, include `affiliateUserId` from cookie in sale request
7. Backend processes 10% affiliate commission

### Registration Flow (with Affiliate)
1. Visitor arrives via affiliate link (affiliate stored in cookie)
2. Visitor registers account
3. Frontend includes `affiliateCode` from cookie in registration request
4. Backend sets affiliate as user's parent (referral relationship)
5. Clear affiliate cookie (relationship now permanent via parent_id)

## Security Considerations

1. **Public Endpoint**: `/api/aff/{code}` is public (no auth required)
2. **Validation**: Backend validates affiliate code and checks user is active
3. **Cookie Security**: Cookies use `SameSite=Lax` to prevent CSRF
4. **Expiry**: 30-day TTL prevents stale affiliate tracking

## Testing Checklist

- [ ] Affiliate code validation works correctly
- [ ] Invalid affiliate codes return 404
- [ ] Affiliate info stored in cookie and localStorage
- [ ] Cookie expires after 30 days
- [ ] Sale creation includes affiliateUserId when available
- [ ] Affiliate commission (10%) calculated correctly
- [ ] Registration with affiliate code sets parent correctly
- [ ] Affiliate cookie cleared after registration
- [ ] Inactive affiliates don't receive commissions

## Notes

- Affiliate commission is **single-level only** (10% flat rate)
- Referral commissions (multi-level) are separate and still work as before
- Affiliate codes are 10 characters long (vs 8 for referral codes)
- Both systems can work simultaneously on the same sale

