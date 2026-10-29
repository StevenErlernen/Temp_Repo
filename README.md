# Black Money Case Initiation REST API

Spring Boot 3 / Java 21 starter based on the supplied Figma screens and database screenshots.

## Important integration assumptions
1. The screenshots show a DB2-like type display (`OCTETS`, `DECIMAL`, `TIMESTAMP`) but do not show the actual table name or constraints. This starter uses DB2 and creates new tables `BM_CASE`, `BM_CASE_NOTING`, and `BM_CASE_ATTACHMENT`. Do not run its migration against a shared/production database until the DBA confirms the database, schema, existing case table, primary key, and migration policy.
2. The visible existing table columns appear to belong to an existing case/workflow table. This starter does **not** map to that table because its table name, primary key, exact numeric precision, and constraints are not visible.
3. `source-of-info` and `assign-to` lookup values are illustrative. Connect them to approved master data / employee-position services before production.
4. `actor` is accepted in the request only as a development placeholder. In production derive the user from authenticated security context; never trust a client-supplied actor.
5. Files are stored locally under `./uploads` with random storage keys. Production should use an approved managed object store or document management system, malware scanning, access controls, retention rules, and cleanup for rolled-back uploads.
6. The current sample associates all uploaded files in a request with the first noting. If files must map to different noting rows, extend the request with a per-file `notingIndex` or upload files in separate requests.
7. Save uses `action: "SAVE"` and Submit uses `action: "SUBMIT"`. Submitted cases become immutable in this starter. Confirm whether that matches the actual workflow.

## Run
Set environment variables:
- `DB_URL` e.g. `jdbc:db2://localhost:50000/BLKMONEY`
- `DB_USERNAME`
- `DB_PASSWORD`
- `FILE_STORAGE_ROOT` (optional)

Then run:
```bash
mvn spring-boot:run
```

## API endpoints

### 1. Source of information
`GET /api/v1/lookups/source-of-info`

### 2. Assign-to options
`GET /api/v1/lookups/assign-to`

### 3. Save or submit case (single endpoint)
`POST /api/v1/black-money-cases` (`multipart/form-data`)
- `request`: JSON part
- `files`: zero or more uploaded files
- `categories`: repeated optional strings in same order as files
- `fileDescriptions`: repeated optional strings in same order as files

Example `request` JSON:
```json
{
  "caseId": null,
  "pan": "ABCDE1234F",
  "uniqueId": null,
  "assesseeName": "Rahul Singh",
  "description": "Other information",
  "sourceOfInfo": "OTHER_AGENCY",
  "assetIncomeDetails": "Details of assets/income",
  "assignTo": "IO19_DELHI",
  "remarks": "Additional remark",
  "action": "SAVE",
  "actor": "employee-id",
  "notings": [
    { "name": "Initial noting", "text": "DAK has been transferred." }
  ]
}
```
Use `"action": "SUBMIT"` to submit. For updates, send the existing `caseId`.

### 4. Read case
`GET /api/v1/black-money-cases/{caseId}`

## cURL example
```bash
curl -X POST http://localhost:8080/api/v1/black-money-cases \
  -F 'request={"caseId":null,"pan":"ABCDE1234F","assesseeName":"Rahul Singh","description":"Other information","sourceOfInfo":"OTHER_AGENCY","assetIncomeDetails":"Foreign bank account","assignTo":"IO19_DELHI","action":"SAVE","actor":"employee-id","notings":[{"name":"Initial noting","text":"Transferred to officer"}]};type=application/json' \
  -F 'files=@./sample.pdf' \
  -F 'categories=SUPPORTING_DOCUMENT' \
  -F 'fileDescriptions=Source document'
```

## Before production
- Confirm actual DB platform/table/schema and map entity fields to the existing table rather than creating duplicate case data.
- Add authentication/authorization (Spring Security/OIDC or approved government identity provider).
- Add audit trail with immutable event records; do not rely only on last-updated columns.
- Move lookup values to master tables/integrations.
- Add download/delete attachment endpoints with authorization and audit events.
- Add idempotency, optimistic locking, malware scanning, API documentation, integration tests, and deployment secrets management.
