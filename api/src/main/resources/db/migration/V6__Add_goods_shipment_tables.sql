alter table warehouse_item_types ADD CONSTRAINT title_unique unique (title);


CREATE TABLE IF NOT EXISTS goods_shipment_document (
    id bigserial PRIMARY KEY NOT NULL,
    version bigint,
    document_date date NOT NULL,
    document_number varchar(255) NOT NULL,
    contractor_id bigint NOT NULL REFERENCES contractors,
    contract_id bigint REFERENCES contracts,
    warehouse_id bigint NOT NULL REFERENCES warehouses,
    conducted boolean NOT NULL DEFAULT false
);


CREATE TABLE IF NOT EXISTS goods_shipment_item (
    id bigserial PRIMARY KEY NOT NULL,
    version bigint,
    document_id bigint NOT NULL REFERENCES goods_shipment_document ON DELETE CASCADE,
    item_type_id bigint NOT NULL REFERENCES warehouse_item_types,
    quantity numeric(24,8) NOT NULL DEFAULT 0.0,
    price numeric(21,5) NOT NULL DEFAULT 0.0,
    total_sum numeric(18,2) NOT NULL DEFAULT 0.0
);

insert into goods_shipment_document (version, document_date, document_number, contractor_id, contract_id, warehouse_id, conducted) values
 (0, DATEADD('DAY', -20, CURRENT_DATE), 'GS-0001', 1,  NULL, 1, FALSE),
 (0, DATEADD('DAY', -19, CURRENT_DATE), 'GS-0002', 2,  NULL, 2, FALSE),
 (0, DATEADD('DAY', -18, CURRENT_DATE), 'GS-0003', 3,  NULL, 3, FALSE),
 (0, DATEADD('DAY', -17, CURRENT_DATE), 'GS-0004', 4,  NULL, 4, FALSE),
 (0, DATEADD('DAY', -16, CURRENT_DATE), 'GS-0005', 5,  NULL, 5, FALSE),
 (0, DATEADD('DAY', -15, CURRENT_DATE), 'GS-0006', 6,  NULL, 6, FALSE),
 (0, DATEADD('DAY', -14, CURRENT_DATE), 'GS-0007', 7,  NULL, 1, FALSE),
 (0, DATEADD('DAY', -13, CURRENT_DATE), 'GS-0008', 8,  NULL, 2, FALSE),
 (0, DATEADD('DAY', -12, CURRENT_DATE), 'GS-0009', 9,  NULL, 3, FALSE),
 (0, DATEADD('DAY', -11, CURRENT_DATE), 'GS-0010', 10, NULL, 4, FALSE),
 (0, DATEADD('DAY', -10, CURRENT_DATE), 'GS-0011', 11, NULL, 5, FALSE),
 (0, DATEADD('DAY', -9,  CURRENT_DATE), 'GS-0012', 12, NULL, 6, FALSE),
 (0, DATEADD('DAY', -8,  CURRENT_DATE), 'GS-0013', 13, NULL, 1, FALSE),
 (0, DATEADD('DAY', -7,  CURRENT_DATE), 'GS-0014', 14, NULL, 2, FALSE),
 (0, DATEADD('DAY', -6,  CURRENT_DATE), 'GS-0015', 15, NULL, 3, FALSE),
 (0, DATEADD('DAY', -5,  CURRENT_DATE), 'GS-0016', 16, NULL, 4, FALSE),
 (0, DATEADD('DAY', -4,  CURRENT_DATE), 'GS-0017', 17, NULL, 5, FALSE),
 (0, DATEADD('DAY', -3,  CURRENT_DATE), 'GS-0018', 18, NULL, 6, FALSE),
 (0, DATEADD('DAY', -2,  CURRENT_DATE), 'GS-0019', 19, NULL, 1, FALSE),
 (0, DATEADD('DAY', -1,  CURRENT_DATE), 'GS-0020', 20, NULL, 2, FALSE);


INSERT INTO goods_shipment_item (version, document_id, item_type_id, quantity, price, total_sum)
SELECT 
  0,
  d.id,
  ((row_number() OVER (ORDER BY d.id) - 1) % 10) + 1 AS item_type_id,
  (row_number() OVER (ORDER BY d.id))::numeric(24,8) AS quantity,
  (5 + (row_number() OVER (ORDER BY d.id)))::numeric(21,5) AS price,
  round((row_number() OVER (ORDER BY d.id)) * (5 + (row_number() OVER (ORDER BY d.id))), 2)::numeric(18,2) AS total_sum
FROM goods_shipment_document d
WHERE d.document_number BETWEEN 'GS-0001' AND 'GS-0020';