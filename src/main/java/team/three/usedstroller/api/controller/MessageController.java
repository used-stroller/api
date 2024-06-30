package team.three.usedstroller.api.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.three.usedstroller.api.domain.Message;
import team.three.usedstroller.api.service.MessageService;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
  private final MessageService messageService;

  @CrossOrigin
  @GetMapping("/{room}")
  public ResponseEntity<List<Message>> getMessage(@PathVariable String room){
    return ResponseEntity.ok(messageService.getMessage(room));
  }
}
