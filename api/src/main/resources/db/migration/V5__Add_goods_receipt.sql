
create table goods_receipts (
    id bigserial primary key not null,
    version bigint,
    doc_date date not null,
    doc_number varchar(255) not null,
    contractor_id bigint not null references contractors,
    contract_id bigint references contracts,
    warehouse_id bigint not null references warehouses,
    is_conducted boolean not null default false
);

create table goods_receipt_items (
    id bigserial primary key not null,
    version bigint,
    document_id bigint not null references goods_receipts on delete cascade,
    item_type_id bigint not null references warehouse_item_types,
    quantity numeric(24,8) not null default 0.0,
    price numeric(21,5) not null default 0.0,
    total_sum numeric(18,2) not null default 0.0
);

insert into goods_receipts (version, doc_date, doc_number, contractor_id, contract_id, warehouse_id, is_conducted) values
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

insert into goods_receipt_items (version, document_id, item_type_id, quantity, price, total_sum)
select 0, gr.id, ((row_number() over (order by gr.id) - 1) % 10) + 1 as item_type_id,
       ((row_number() over (order by gr.id)) * 2)::numeric(24,8) as quantity,
       (10 + (row_number() over (order by gr.id)))::numeric(21,5) as price,
       round(((row_number() over (order by gr.id)) * 2) * (10 + (row_number() over (order by gr.id))), 2)::numeric(18,2) as total_sum
from goods_receipts gr
where gr.doc_number between 'GR-0001' and 'GR-0020';


