package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.ArmPivotSubsystem;

public class AutoPivot extends CommandBase {

  private final ArmPivotSubsystem armPivotSubsystem;
  private final PIDController pidController;
  private final Joystick joyArm = new Joystick(Constants.OIConstants.kArmJoystickPort);

  public AutoPivot(ArmPivotSubsystem armPivotSubsystem, double setpoint) {
    this.armPivotSubsystem = armPivotSubsystem;
    this.pidController = new PIDController(Constants.ArmPivotConstants.kPButton,Constants.ArmPivotConstants.kIButton,Constants.ArmPivotConstants.kDButton);
    pidController.setSetpoint(setpoint);
    addRequirements(armPivotSubsystem);
}

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    pidController.reset();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double speed = pidController.calculate(armPivotSubsystem.getEncoderMeters());
    armPivotSubsystem.setMotor(speed);
    System.out.println("Arm position = " + armPivotSubsystem.getEncoderMeters() );
    System.out.println("Arm speed = " + speed );
    System.out.println("joyArm.getRawAxis = " + Math.abs(joyArm.getRawAxis(Constants.OIConstants.kArmPivotAxis)));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    armPivotSubsystem.setMotor(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Math.abs(joyArm.getRawAxis(Constants.OIConstants.kArmPivotAxis)) > 0.05) {
      System.out.println("AutoPivot complete");
      return true;
    } else {
      return false;
    }
 }
}
