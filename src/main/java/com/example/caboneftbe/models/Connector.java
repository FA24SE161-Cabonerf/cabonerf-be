package com.example.caboneftbe.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Connector extends Base{

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
