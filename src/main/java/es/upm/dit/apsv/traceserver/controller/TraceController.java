package es.upm.dit.apsv.traceserver.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.upm.dit.apsv.traceserver.model.Trace;
import es.upm.dit.apsv.traceserver.repository.TraceRepository;

@RestController
public class TraceController {

    private final TraceRepository tr;

    public TraceController(TraceRepository tr) {
        this.tr = tr;
    }

    @GetMapping("/traces")
    public List<Trace> all() {
        return (List<Trace>) tr.findAll();
    }

    @PostMapping("/traces")
    public Trace newTrace(@RequestBody Trace newTrace) {
        return tr.save(newTrace);
    }

    @GetMapping("/traces/{id}")
    public ResponseEntity<Trace> one(@PathVariable String id) {
        Optional<Trace> trace = tr.findById(id);
        return trace.map(t -> ResponseEntity.ok().body(t))
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/traces/{id}")
    public Trace replaceTrace(@RequestBody Trace newTrace, @PathVariable String id) {
        return tr.findById(id).map(trace -> {
            trace.setTruck(newTrace.getTruck());
            trace.setLastSeen(newTrace.getLastSeen());
            trace.setLat(newTrace.getLat());
            trace.setLng(newTrace.getLng());
            return tr.save(trace);
        }).orElseGet(() -> {
            newTrace.setTraceId(id);
            return tr.save(newTrace);
        });
    }

    @DeleteMapping("/traces/{id}")
    public void deleteTrace(@PathVariable String id) {
        tr.deleteById(id);
    }
}
