package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.Jogo;
import pt.isec.pa.deepsea.model.data.Direcoes;
import pt.isec.pa.deepsea.model.data.Drone;
import pt.isec.pa.deepsea.model.data.ElemFosso;
import pt.isec.pa.deepsea.model.data.ExpedicaoHelper;
import pt.isec.pa.deepsea.model.data.Rocha;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;

public class EstadoSubida extends DeepSeaStateAdapter {

    public EstadoSubida(DeepSeaContext context, Jogo modelo) {
        super(context, modelo);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.SUBIDA;
    }

    @Override
    public boolean mover(Direcoes direcao) {
        Drone drone = context.getDrone();
        if (drone == null) {
            return false;
        }

        boolean moveu = false;
        int linhaAtual = drone.getLinhaPos();
        int colunaAtual = drone.getColunaPos();
        int larguraFosso = modelo.getFosso().getLargura();

        if (direcao == Direcoes.CIMA) {
            int novaLinha = linhaAtual - 1;

            if (novaLinha >= 0) {
                ElemFosso destino = modelo.getFosso().getCont(novaLinha, colunaAtual);
                drone.consumirCombustivel(1);
                moveu = true;

                if (destino instanceof Rocha) {
                    destino.interage(drone);
                } else {
                    drone.setPosicao(novaLinha, colunaAtual);
                    if (destino != null) {
                        destino.interage(drone);
                    }
                }
            } else {
                drone.consumirCombustivel(1);
                if (drone.getIntegridadeCasco() > 0) {
                    modelo.getNavio().adicionarMinerios(drone.getMineriosTransp());
                    modelo.getNavio().recolherArtefactosDoDrone(drone);
                    drone.descarregarMinerios();
                    drone.descarregarArtefactos();
                    drone.setPosicao(modelo.getNavio().getLinhaPos(), modelo.getNavio().getColunaPos());
                    modelo.limparZonaExpedicao();
                    DeepSeaState vitoria = ExpedicaoHelper.verificarVitoria(modelo);
                    changeState(vitoria != null ? vitoria : DeepSeaState.SUPERFICIE);
                } else {
                    changeState(ExpedicaoHelper.processarDronePerdido(modelo, drone));
                }
                return true;
            }
        } else if (direcao == Direcoes.ESQUERDA) {
            int novaColuna = colunaAtual - 1;
            if (novaColuna >= 0) {
                ElemFosso destino = modelo.getFosso().getCont(linhaAtual, novaColuna);
                drone.consumirCombustivel(1);
                moveu = true;

                if (destino instanceof Rocha) {
                    destino.interage(drone);
                } else {
                    drone.setPosicao(linhaAtual, novaColuna);
                    if (destino != null) {
                        destino.interage(drone);
                    }
                }
            }
        } else if (direcao == Direcoes.DIREITA) {
            int novaColuna = colunaAtual + 1;
            if (novaColuna < larguraFosso) {
                ElemFosso destino = modelo.getFosso().getCont(linhaAtual, novaColuna);
                drone.consumirCombustivel(1);
                moveu = true;

                if (destino instanceof Rocha) {
                    destino.interage(drone);
                } else {
                    drone.setPosicao(linhaAtual, novaColuna);
                    if (destino != null) {
                        destino.interage(drone);
                    }
                }
            }
        }

        if (!moveu) {
            return false;
        }

        if (drone.getIntegridadeCasco() <= 0 || drone.getCombustivel() <= 0) {
            changeState(ExpedicaoHelper.processarDronePerdido(modelo, drone));
            return true;
        }
        return true;
    }
}
