ALTER TABLE produto ADD COLUMN estoque_minimo INT NOT NULL DEFAULT 0;
ALTER TABLE produto ADD COLUMN preco_custo DECIMAL(15,2);

ALTER TABLE venda ADD COLUMN valor_total_lucro DECIMAL(15,2);
ALTER TABLE venda ADD COLUMN forma_pagamento VARCHAR(20);
ALTER TABLE venda ADD COLUMN status_pagamento VARCHAR(20);