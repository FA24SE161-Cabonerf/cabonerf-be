package com.example.cabonerfbe.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Connector extends Base {

    @ManyToOne
    @JoinColumn(name = "start_process_id")
    private Process startProcess;

    @ManyToOne
    @JoinColumn(name = "end_process_id")
    private Process endProcess;

    @ManyToOne
    @JoinColumn(name = "start_exchanges_id")
    private Exchanges startExchanges;

    @ManyToOne
    @JoinColumn(name = "end_exchanges_id")
    private Exchanges endExchanges;
}
