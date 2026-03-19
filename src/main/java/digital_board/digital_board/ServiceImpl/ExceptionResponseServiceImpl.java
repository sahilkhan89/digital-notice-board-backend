package digital_board.digital_board.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import digital_board.digital_board.Entity.ExceptionResponse;
import digital_board.digital_board.Repository.ExceptionResponseRepository;
import digital_board.digital_board.Servies.ExceptionResponseService;

@Service
public class ExceptionResponseServiceImpl implements ExceptionResponseService {

    @Autowired
    private ExceptionResponseRepository exceptionResponseRepository;

    @Override
    public List<ExceptionResponse> GetAllMassage() {

        return exceptionResponseRepository.findAll();
    }

}
