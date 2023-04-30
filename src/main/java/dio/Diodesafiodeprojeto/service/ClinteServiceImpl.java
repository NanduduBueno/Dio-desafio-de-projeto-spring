package dio.Diodesafiodeprojeto.service;

import dio.Diodesafiodeprojeto.model.Cliente;
import dio.Diodesafiodeprojeto.model.ClienteRepository;
import dio.Diodesafiodeprojeto.model.Endereco;
import dio.Diodesafiodeprojeto.model.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.plaf.OptionPaneUI;
import java.util.Optional;

@Service
public class ClinteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    @Override
    public Iterable<Cliente> buscarTodos(){
        return clienteRepository.findAll();
    }
    @Override
    public Cliente buscarPorId(Long id){
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }
    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);
    }
    @Override
    public void atualizar(Long id, Cliente cliente){
    Optional<Cliente> clienteBd = clienteRepository.findById(id);
    if (clienteBd.isPresent()) {
        salvarClienteComCep(cliente);
        }
    }
    @Override
    public void deletar(Long id){
        clienteRepository.deleteById(id);
    }
    private void salvarClienteComCep(Cliente cliente){
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(Long.valueOf(cep)).orElseGet(() ->{
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return null;
        });
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }
}
